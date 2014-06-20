package es.uvigo.ei.sing.mla.daos;

import es.uvigo.ei.sing.mla.model.entities.Replicate;

public interface ReplicateDAO {
	public Replicate add(Replicate replicate);

	public Replicate get(Integer id);

	public Replicate reload(Replicate replicate);

	public Replicate update(Replicate replicate);

	public void delete(Replicate replicate);
}
