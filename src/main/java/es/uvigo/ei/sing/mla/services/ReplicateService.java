package es.uvigo.ei.sing.mla.services;

import es.uvigo.ei.sing.mla.model.entities.Replicate;

public interface ReplicateService {
	public Replicate add(Replicate replicate);

	public Replicate get(Integer id);

	public Replicate update(Replicate replicate);

	public void delete(Replicate replicate);
}
