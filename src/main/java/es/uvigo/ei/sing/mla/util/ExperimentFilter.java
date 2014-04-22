package es.uvigo.ei.sing.mla.util;

import es.uvigo.ei.sing.mla.model.entities.User;

public class ExperimentFilter {
	User user = new User();
	String name = "";

	public ExperimentFilter(User user) {
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
