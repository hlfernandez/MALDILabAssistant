package es.uvigo.ei.sing.mla.model.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import es.uvigo.ei.sing.mla.daos.UserDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/daos.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class UserTest {
	@Autowired
	private UserDAO dao;

	@Test
	public void testUserAddExperimentThrowsException() {
		Experiment experiment = null;

		assertThat(dao.get("pepe").addExperiment(experiment)).isInstanceOf(
				NullPointerException.class);
	}

	@Test
	public void testUserAddExperimentReturnsTrue() {
		Experiment experiment = new Experiment();

		assertThat(dao.get("pepe").addExperiment(experiment)).isTrue();
	}

	@Test
	public void testUserAddExperimentReturnsFalse() {
		Experiment experiment = dao.get("pepe").getExperiments().get(0);

		assertThat(dao.get("pepe").addExperiment(experiment)).isFalse();
	}

	@Test
	public void testUserRemoveExperimentThrowsException() {
		Experiment experiment = null;

		assertThat(dao.get("pepe").removeExperiment(experiment)).isInstanceOf(
				NullPointerException.class);
	}

	@Test
	public void testUserRemoveExperimentReturnsTrue() {
		Experiment experiment = dao.get("pepe").getExperiments().get(0);

		assertThat(dao.get("pepe").removeExperiment(experiment)).isTrue();
	}

	@Test
	public void testUserRemoveExperimentReturnsFalse() {
		Experiment experiment = new Experiment();

		assertThat(dao.get("pepe").removeExperiment(experiment)).isFalse();
	}
}
