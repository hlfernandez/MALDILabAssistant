package es.uvigo.ei.sing.mla.model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
public class ConditionGroup extends Observable {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 32)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "experimentId")
	private Experiment experiment;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "condition", cascade = CascadeType.ALL)
	private List<Sample> samples;

	@Column(length = 7)
	private String color;

	public ConditionGroup() {
		this.name = "";
		this.experiment = null;
		this.samples = new LinkedList<>();
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

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		if (this.experiment != null) {
			this.experiment._removeCondition(this);
		}
		this.experiment = experiment;
		if (this.experiment != null) {
			this.experiment._addCondition(this);
		}
	}

	public List<Sample> getSamples() {
		return Collections.unmodifiableList(this.samples);
	}

	public List<Replicate> getReplicates() {
		List<Replicate> replicates = new ArrayList<Replicate>();

		for(Sample sample : this.getSamples()) {
			replicates.addAll(sample.getReplicates());
		}

		return replicates;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int countReplicates() {
		int count = 0;

		for (Sample sample : this.getSamples()) {
			count += sample.countReplicates();
		}

		return count;
	}

	public boolean addSample(Sample sample) {
		Objects.requireNonNull(sample, "sample can't be null");

		if (!this.samples.contains(sample)) {
			sample.setCondition(this);

			return true;
		} else {
			return false;
		}
	}

	public boolean removeSample(Sample sample) {
		Objects.requireNonNull(sample, "sample can't be null");

		if (this.equals(sample.getCondition())) {
			sample.setCondition(null);

			return true;
		} else {
			return false;
		}
	}

	void _addSample(Sample sample) {
		this.samples.add(sample);

		this.setChanged();
		this.notifyObservers(sample);
	}

	void _removeSample(Sample sample) {
		this.samples.remove(sample);

		this.setChanged();
		this.notifyObservers(sample);
	}

	public boolean isOnPlate() {
		if (samples.isEmpty()) {
			return false;
		}

		for (Sample sample : samples) {
			if (!sample.isOnPlate()) {
				return false;
			}
		}

		return true;
	}
}
