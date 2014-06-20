package es.uvigo.ei.sing.mla.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.ei.sing.mla.model.entities.Sample;

@Repository
public class SampleDAOImpl implements SampleDAO {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public Sample add(Sample sample) {
		em.persist(sample);

		return sample;
	}

	@Override
	@Transactional(readOnly = true)
	public Sample get(Integer id) {
		return em.find(Sample.class, id);
	}

	@Override
	@Transactional
	public Sample update(Sample sample) {
		em.merge(sample);

		return sample;
	}

	@Override
	@Transactional
	public void delete(Sample sample) {
		em.remove(sample);
	}

	@Override
	@Transactional(readOnly = true)
	public Sample reload(Sample sample) {
		em.detach(sample);
		return this.get(sample.getId());
	}
}
