package es.uvigo.ei.sing.mla.daos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.User;
import es.uvigo.ei.sing.mla.services.ExperimentSearchFilter;

@Repository
public class ExperimentDAOImpl implements ExperimentDAO {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@Transactional
	public Experiment add(Experiment experiment) {
		em.persist(experiment);

		return experiment;
	}

	@Override
	@Transactional(readOnly = true)
	public Experiment get(Integer id) {
		return em.find(Experiment.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public Experiment reload(Experiment experiment) {
		em.detach(experiment);
		return this.get(experiment.getId());
	}

	@Override
	@Transactional
	public Experiment update(Experiment experiment) {
		em.merge(experiment);

		return experiment;
	}

	@Override
	@Transactional
	public void delete(Experiment experiment) {
		em.remove(this.get(experiment.getId()));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Experiment> listFilter(ExperimentSearchFilter filter) {
		List<Experiment> filterExperiments = new ArrayList<Experiment>();
		String name = filter.getName().toLowerCase();
		User user = this.em.find(User.class, filter.getUser().getLogin());

		for (Experiment exp : user.getExperiments()) {
			if (exp.getName() != null
					&& exp.getName().toLowerCase().contains(name)) {
				filterExperiments.add(exp);
			}
		}

		return filterExperiments;
	}
}
