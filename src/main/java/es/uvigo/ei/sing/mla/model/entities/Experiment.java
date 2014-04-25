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

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
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
}
