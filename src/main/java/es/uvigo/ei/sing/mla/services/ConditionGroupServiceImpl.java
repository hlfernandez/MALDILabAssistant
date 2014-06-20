package es.uvigo.ei.sing.mla.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.uvigo.ei.sing.mla.daos.ConditionGroupDAO;
import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;

@Service("conditionGroupService")
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConditionGroupServiceImpl implements ConditionGroupService {
	@Autowired
	ConditionGroupDAO dao;

	@Override
	public ConditionGroup add(ConditionGroup condition) {
		return dao.add(condition);
	}

	@Override
	public ConditionGroup get(Integer id) {
		return dao.get(id);
	}

	@Override
	public ConditionGroup update(ConditionGroup condition) {
		return dao.update(condition);
	}

	@Override
	public void delete(ConditionGroup condition) {
		dao.delete(condition);
	}
}
