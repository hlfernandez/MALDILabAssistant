package es.uvigo.ei.sing.mla.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Sample extends Observable {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 32)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "conditionId")
	private ConditionGroup condition;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sample", cascade = CascadeType.ALL)
	private List<Replicate> replicates;

	public Sample() {
		this.name = "";
		this.condition = null;
		this.replicates = new ArrayList<>();
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

	public ConditionGroup getCondition() {
		return this.condition;
	}

	public void setCondition(ConditionGroup condition) {
		if (this.condition != null) {
			this.condition._removeSample(this);
		}

		this.condition = condition;

		if (this.condition != null) {
			this.condition._addSample(this);
		}
	}

	public List<Replicate> getReplicates() {
		return Collections.unmodifiableList(this.replicates);
	}

	public int countReplicates() {
		return this.getReplicates().size();
	}

	public boolean addReplicate(Replicate replicate) {
		Objects.requireNonNull(replicate, "replicate can't be null");

		if (!this.replicates.contains(replicate)) {
			replicate.setSample(this);

			return true;
		} else {
			return false;
		}
	}

	public boolean removeReplicate(Replicate replicate) {
		Objects.requireNonNull(replicate, "replicate can't be null");

		if (this.equals(replicate.getSample())) {
			replicate.setSample(null);

			return true;
		} else {
			return false;
		}
	}

	void _addReplicate(Replicate replicate) {
		this.replicates.add(replicate);

		this.setChanged();
		this.notifyObservers(replicate);
	}

	void _removeReplicate(Replicate replicate) {
		this.replicates.remove(replicate);

		this.setChanged();
		this.notifyObservers(replicate);
	}

	public boolean isOnPlate() {
		for (Replicate replicate : replicates) {
			if (!replicate.isOnPlate()) {
				return false;
			}
		}

		return true;
	}

	public String getColor() {
		if (this.getCondition() != null) {
			return this.getCondition().getColor();
		} else {
			return null;
		}
	}
}
