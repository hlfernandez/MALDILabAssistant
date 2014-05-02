package es.uvigo.ei.sing.mla.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Replicate {
	@Id
	@GeneratedValue
	private Integer id;

	@Column(length = 32)
	private String name;

	private int plateId = 0;
	private int col = 0;
	private int row = 0;

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

	public int getPlateId() {
		return plateId;
	}

	public void setPlateId(int plateId) {
		this.plateId = plateId;
	}

	public boolean isOnPlate() {
		return plateId > 0;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
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
}
