package es.uvigo.ei.sing.mla.daos;

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

import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.User;
import es.uvigo.ei.sing.mla.services.ExperimentSearchFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ExperimentDAOTest {
	@Autowired
	private ExperimentDAO experimentDAO;

	@Autowired
	private UserDAO userDAO;

	@Test
	public void testExperimentDAOCanAdd() {
		Experiment experiment = new Experiment();

		experimentDAO.add(experiment);

		assertThat(experimentDAO.get(experiment.getId())).isNotNull();
	}

	@Test
	public void testExperimentDAOCanGet() {
		assertThat(experimentDAO.get(1)).isNotNull();
	}

	@Test
	public void testExperimentDAOCanReload() {
		Experiment experiment = experimentDAO.get(2);
		experiment.setName("New Experiment 2");

		experimentDAO.reload(experiment);

		assertThat(experimentDAO.get(experiment.getId()).getName()).isEqualTo("Experiment 2");
	}

	@Test
	public void testExperimentDAOCanUpdate() {
		Experiment experiment = experimentDAO.get(1);
		experiment.setName("New Experiment 1");

		experimentDAO.update(experiment);

		assertThat(experimentDAO.get(experiment.getId()).getName()).isEqualTo(
				"New Experiment 1");
	}

	@Test
	public void testExperimentDAOCanDelete() {
		Experiment experiment = experimentDAO.get(1);

		experimentDAO.delete(experiment);

		assertThat(experimentDAO.get(1)).isNull();
	}

	@Test
	public void testExperimentDAOCanList() {
		User user = userDAO.get("pepe");

		assertThat(experimentDAO.list(user).size()).isEqualTo(2);
	}

	@Test
	public void testExperimentDAOCanListFilter() {
		User user = userDAO.get("pepe");
		ExperimentSearchFilter filter = new ExperimentSearchFilter(user);
		filter.setName("Experiment 2");

		assertThat(experimentDAO.listFilter(filter).size()).isEqualTo(1);
	}
}
