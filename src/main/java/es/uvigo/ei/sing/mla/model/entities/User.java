package es.uvigo.ei.sing.mla.model.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import es.uvigo.ei.sing.mla.util.Configuration;

@Entity
public class User {
	@Id
	@Column(length = 32, nullable = false)
	private String login;

	@Column(length = 64, nullable = false)
	private String password;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private List<Experiment> experiments;

	private String directory;

	public User() {
		this(null, null);
	}

	public User(String login, String password) {
		this.login = login;
		this.password = password;
		this.experiments = new ArrayList<>();
		this.directory = new File(Configuration.getInstance()
				.getUsersDirectory(), login).getPath();
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		if (!this.login.equals(login)) {
			this.setDirectory(login);

			this.login = login;
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Experiment> getExperiments() {
		return Collections.unmodifiableList(this.experiments);
	}

	public File getDirectory() {
		return new File(this.directory);
	}

	public void setDirectory(String login) {
		File newDirectory = new File(this.directory);
		newDirectory.renameTo(new File(login));

		this.directory = newDirectory.getPath();
	}

	public boolean addExperiment(Experiment experiment) {
		Objects.requireNonNull(experiment, "experiment can't be null");

		if (!this.experiments.contains(experiment)) {
			experiment.setUser(this);

			return true;
		} else {
			return false;
		}
	}

	public boolean removeExperiment(Experiment experiment) {
		Objects.requireNonNull(experiment, "experiment can't be null");

		if (this.equals(experiment.getUser())) {
			experiment.setUser(null);

			return true;
		} else {
			return false;
		}
	}

	void _addExperiment(Experiment experiment) {
		this.experiments.add(experiment);
	}

	void _removeExperiment(Experiment experiment) {
		this.experiments.remove(experiment);
	}
}
