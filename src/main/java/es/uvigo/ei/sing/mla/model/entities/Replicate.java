package es.uvigo.ei.sing.mla.model.entities;

import java.util.Observable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Replicate extends Observable {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 32)
	private String name;

	@Column(nullable = true)
	private Integer plateId;
	@Column(nullable = true)
	private Integer col;
	@Column(nullable = true)
	private Integer row;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sampleId")
	private Sample sample;

	public Replicate() {
		this.name = "";
		this.sample = null;
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

	public Integer getPlateId() {
		return plateId;
	}

	public Integer getCol() {
		return col;
	}

	public Integer getRow() {
		return row;
	}

	public Sample getSample() {
		return this.sample;
	}

	public void setSample(Sample sample) {
		if (this.sample != null) {
			this.sample._removeReplicate(this);
		}

		this.sample = sample;

		if (this.sample != null) {
			this.sample._addReplicate(this);
		}
	}

	public boolean isOnPlate() {
		return this.plateId != null && this.col != null && this.row != null;
	}
	
	public boolean isOnPlate(int plateId) {
		return this.getPlateId() != null && this.getPlateId() == plateId;
	}
	
	public boolean isPlacedAt(int plateId, int row, int col) {
		return this.getPlateId() != null && this.getPlateId() == plateId && 
			this.getRow() != null && this.getRow() == row && 
			this.getCol() != null && this.getCol() == col;
	}
	
	public void removeFromPlate() {
		this.plateId = null;
		this.col = null;
		this.row = null;
		
		this.setChanged();
		this.notifyObservers();
	}

	void placeAtPlate(int plateId, int row, int col) {
		this.plateId = plateId;
		this.row = row;
		this.col = col;
		
		this.setChanged();
		this.notifyObservers();
	}
	
	public String getColor() {
		if (this.getSample() != null) {
			return this.getSample().getColor();
		} else {
			return null;
		}
	}
}
