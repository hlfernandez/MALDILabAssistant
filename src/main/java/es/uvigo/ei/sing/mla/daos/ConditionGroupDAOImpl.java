package es.uvigo.ei.sing.mla.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;

@Repository
public class ConditionGroupDAOImpl implements ConditionGroupDAO {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public ConditionGroup add(ConditionGroup condition) {
		em.persist(condition);

		return condition;
	}

	@Override
	@Transactional(readOnly = true)
	public ConditionGroup get(Integer id) {
		return em.find(ConditionGroup.class, id);
	}

	@Override
	@Transactional
	public ConditionGroup update(ConditionGroup condition) {
		em.merge(condition);

		return condition;
	}

	@Override
	@Transactional
	public void delete(ConditionGroup condition) {
		em.remove(condition);
	}

	@Override
	@Transactional(readOnly = true)
	public ConditionGroup reload(ConditionGroup condition) {
		em.detach(condition);
		return this.get(condition.getId());
	}
}
