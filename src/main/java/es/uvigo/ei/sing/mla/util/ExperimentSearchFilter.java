package es.uvigo.ei.sing.mla.util;

import es.uvigo.ei.sing.mla.model.entities.User;

public class ExperimentSearchFilter {
	User user = new User();
	String name = "";

	public ExperimentSearchFilter(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
