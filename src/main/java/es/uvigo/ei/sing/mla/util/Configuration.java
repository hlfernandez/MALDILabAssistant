package es.uvigo.ei.sing.mla.util;

import java.io.File;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class Configuration {
	private static Configuration instance = new Configuration();

	public static Configuration getInstance() {
		return Configuration.instance;
	}

	private InitialContext initialContext;

	private Configuration() {
	}

	private synchronized void createInitialContext() throws NamingException {
		if (this.initialContext == null)
			this.initialContext = new InitialContext();
	}

	@SuppressWarnings("unchecked")
	private <T> T getConfigParam(String param) {
		param = "mla." + param;
		try {
			if (this.initialContext == null)
				this.createInitialContext();

			return (T) this.initialContext.lookup("java:comp/env/" + param);
		} catch (NamingException e) {
			return null;
		}
	}

	public File getDataDirectory() {
		final String directory = this
				.getConfigParam("storage.files.dataDirectory");
		return new File(directory);
	}

	public File getUsersDirectory() {
		final String file = this.getConfigParam("storage.files.usersDirectory");
		return new File(this.getDataDirectory(), file);
	}

	public File getTmpDirectory() {
		final String file = this.getConfigParam("storage.files.tmpDirectory");
		return new File(this.getDataDirectory(), file);
	}
}
