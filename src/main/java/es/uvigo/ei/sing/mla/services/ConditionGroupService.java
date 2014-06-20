package es.uvigo.ei.sing.mla.services;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;

public interface ConditionGroupService {
	public ConditionGroup add(ConditionGroup condition);

	public ConditionGroup get(Integer id);

	public ConditionGroup update(ConditionGroup condition);

	public void delete(ConditionGroup condition);
}
