package es.uvigo.ei.sing.mla.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uvigo.ei.sing.mla.util.CellNameType;

@Entity
public class Experiment extends Observable {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 32)
	private String name;

	@Column(length = 500)
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	private int numRows;

	private int numCols;

	@Enumerated(EnumType.STRING)
	private CellNameType rowNameType;

	@Enumerated(EnumType.STRING)
	private CellNameType colNameType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login")
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "experiment", cascade = CascadeType.ALL)
	private List<ConditionGroup> conditions;

	@Lob
	private byte[] file;

	public Experiment() {
		this.name = "";
		this.description = "";
		this.startDate = new Date();
		this.endDate = new Date();
		this.numRows = 10;
		this.numCols = 10;
		this.rowNameType = CellNameType.UPPERCASE;
		this.colNameType = CellNameType.NUMERICAL;
		this.user = null;
		this.conditions = new ArrayList<>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) throws IllegalArgumentException {
		if (numRows <= 0) throw new IllegalArgumentException("numRows can't be <= 0");
		
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) throws IllegalArgumentException {
		if (numCols <= 0) throw new IllegalArgumentException("numCols can't be <= 0");
		
		this.numCols = numCols;
	}

	public CellNameType getRowNameType() {
		return rowNameType;
	}

	public void setRowNameType(CellNameType rowNameType) {
		this.rowNameType = rowNameType;
	}

	public CellNameType getColNameType() {
		return colNameType;
	}

	public void setColNameType(CellNameType colNameType) {
		this.colNameType = colNameType;
	}

	public User getUser() {
		return this.user;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public int countReplicates() {
		int count = 0;

		for (ConditionGroup condition : this.getConditions()) {
			count += condition.countReplicates();
		}

		return count;
	}

	public void setUser(User user) {
		if (this.user != null) {
			this.user._removeExperiment(this);
		}

		this.user = user;

		if (this.user != null) {
			this.user._addExperiment(this);
		}
	}

	public List<ConditionGroup> getConditions() {
		return Collections.unmodifiableList(conditions);
	}

	public List<Replicate> getReplicates() {
		List<Replicate> replicates = new ArrayList<Replicate>();

		for (ConditionGroup condition : this.conditions) {
			for (Sample sample : condition.getSamples()) {
				replicates.addAll(sample.getReplicates());
			}
		}

		return replicates;
	}

	public List<Replicate> getReplicates(int plateId) {
		List<Replicate> replicates = new ArrayList<Replicate>();

		for (ConditionGroup condition : this.conditions) {
			for (Sample sample : condition.getSamples()) {
				for (Replicate replicate : sample.getReplicates()) {
					if (replicate.getPlateId() != null
							&& replicate.getPlateId() == plateId)
						replicates.add(replicate);
				}
			}
		}

		return replicates;
	}

	public List<Sample> getSamples() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addCondition(ConditionGroup condition) {
		Objects.requireNonNull(condition, "condition can't be null");

		condition.setExperiment(this);
	}

	public boolean removeCondition(ConditionGroup condition) {
		Objects.requireNonNull(condition, "condition can't be null");

		if (this.conditions.contains(condition)) {
			condition.setExperiment(null);

			return true;
		} else {
			return false;
		}
	}

	void _addCondition(ConditionGroup conditionGroup) {
		this.conditions.add(conditionGroup);

		this.setChanged();
		this.notifyObservers(conditionGroup);
	}

	void _removeCondition(ConditionGroup conditionGroup) {
		this.conditions.remove(conditionGroup);

		this.setChanged();
		this.notifyObservers(conditionGroup);
	}

	public boolean isOnPlate() {
		if (conditions.isEmpty()) {
			return false;
		}

		for (ConditionGroup condition : conditions) {
			if (!condition.isOnPlate()) {
				return false;
			}
		}

		return true;
	}

	public boolean isMetadataComplete() {
		return this.getNumRows() > 0 && this.getNumCols() > 0
				&& this.getName() != null 
				&& !this.getName().isEmpty()
				&& !this.getConditions().isEmpty()
				&& this.eachSampleHasReplicates();
	}

	private boolean eachSampleHasReplicates() {
		for (ConditionGroup condition : this.getConditions()) {
			if (condition.getSamples().isEmpty()) {
				return false;
			} else {
				for (Sample sample : condition.getSamples()) {
					if (sample.getReplicates().isEmpty())
						return false;
				}
			}
		}

		return true;
	}

	public void placeReplicateAt(Replicate replicate, int plateId, int row,
			int col) {
		Objects.requireNonNull(replicate, "replicate can't be null");
		validatePlateLocation(plateId, row, col);

		if (!replicate.isPlacedAt(plateId, row, col)) {
			for (Replicate rep : this.getReplicates()) {
				if (!rep.equals(replicate) && rep.isPlacedAt(plateId, row, col)) {
					rep.removeFromPlate();
					break;
				}
			}

			replicate.placeAtPlate(plateId, row, col);
		}
	}

	public Replicate getReplicateAt(int plateId, int row, int col) {
		validatePlateLocation(plateId, row, col);

		for (Replicate replicate : this.getReplicates(plateId)) {
			if (replicate.isPlacedAt(plateId, row, col))
				return replicate;
		}

		return null;
	}

	private void validatePlateLocation(int plateId, int row, int col) {
		if (plateId < 0)
			throw new IllegalArgumentException("plateId can't be < 0");
		if (row < 0)
			throw new IllegalArgumentException("row can't be < 0");
		else if (row > this.getNumRows())
			throw new IllegalArgumentException(
					"row can't be greater than the number of rows");
		if (col < 0)
			throw new IllegalArgumentException("row can't be < 0");
		else if (col > this.getNumCols())
			throw new IllegalArgumentException(
					"col can't be greater than the number of columns");
	}
}
