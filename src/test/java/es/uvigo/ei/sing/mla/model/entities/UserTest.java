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
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class UserTest {
	@Autowired
	private UserDAO dao;

	@Test(expected = NullPointerException.class)
	public void testUserAddNullExperimentThrowsException() {
		dao.get("pepe").addExperiment(null);
	}

	@Test
	public void testUserCanAddExperiment() {
		User user = dao.get("pepe");

		Experiment experiment = new Experiment();
		assertThat(user.addExperiment(experiment)).isTrue();

		experiment = user.getExperiments().get(0);
		assertThat(user.addExperiment(experiment)).isFalse();
	}

	@Test(expected = NullPointerException.class)
	public void testUserRemoveNullExperimentThrowsException() {
		dao.get("pepe").removeExperiment(null);
	}

	@Test
	public void testUserCanRemoveExperiment() {
		User user = dao.get("pepe");

		Experiment experiment = user.getExperiments().get(0);
		assertThat(user.removeExperiment(experiment)).isTrue();

		experiment = new Experiment();
		assertThat(user.removeExperiment(experiment)).isFalse();
	}
}
