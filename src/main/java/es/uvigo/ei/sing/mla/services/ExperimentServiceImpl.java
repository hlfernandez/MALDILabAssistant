package es.uvigo.ei.sing.mla.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.uvigo.ei.sing.mla.daos.ExperimentDAO;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.User;

@Service("experimentService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ExperimentServiceImpl implements ExperimentService {
	@Autowired
	ExperimentDAO dao;

	@Override
	public Experiment add(Experiment experiment) {
		return dao.add(experiment);
	}

	@Override
	public Experiment get(int experimentId) {
		return dao.get(experimentId);
	}

	@Override
	public Experiment update(Experiment experiment) {
		return dao.update(experiment);
	}

	@Override
	public void delete(Experiment experiment) {
		dao.delete(experiment);
	}

	@Override
	public List<Experiment> list(User user) {
		return dao.list(user);
	}
}
