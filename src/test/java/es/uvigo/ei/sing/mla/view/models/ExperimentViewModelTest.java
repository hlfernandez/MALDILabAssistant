package es.uvigo.ei.sing.mla.view.models;

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
import es.uvigo.ei.sing.mla.services.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ExperimentViewModelTest {
	@Autowired
	private ExperimentViewModel model;

	@Autowired
	private ExperimentService service;

	@Test
	public void testExperimentViewModelCanGetPlateIds() {
		Experiment experiment = service.get(1);

		final int cols = experiment.getNumCols();
		final int rows = experiment.getNumRows();
		final int replicates = experiment.countReplicates();

		model.setExperiment(experiment);

		final int numPlates = (int) Math.ceil((double) replicates / (double) (cols * rows));

		assertThat(model.getPlateIds().size()).isEqualTo(numPlates);
	}

	@Test
	public void testExperimentViewModelCanGetPlateNames() {
		Experiment experiment = service.get(1);

		final int cols = experiment.getNumCols();
		final int rows = experiment.getNumRows();
		final int replicates = experiment.countReplicates();

		model.setExperiment(experiment);

		final int numPlates = (int) Math.ceil((double) replicates / (double) (cols * rows));

		assertThat(model.getPlateNames().size()).isEqualTo(numPlates);
	}
}
