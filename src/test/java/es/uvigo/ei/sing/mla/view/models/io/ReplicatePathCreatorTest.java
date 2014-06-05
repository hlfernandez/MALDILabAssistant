package es.uvigo.ei.sing.mla.view.models.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;
import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;
import es.uvigo.ei.sing.mla.services.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ReplicatePathCreatorTest {
	@Autowired
	ExperimentService service;

	@Test
	public void testReplicatePathCreatorCanCreate() throws IOException {
		final Experiment experiment = service.get(3);

		ReplicatePathCreator pathCreator = new ReplicatePathCreator(experiment, new File("src/test/resources/dataset"));

		pathCreator.create(new File("src/test/resources/tmp"), new ExperimentListFilter() {
			@Override
			public List<ConditionGroup> listConditions() {
				return experiment.getConditions();
			}

			@Override
			public List<Sample> listSamples() {
				return experiment.getSamples();
			}

			@Override
			public List<Replicate> listReplicates() {
				return experiment.getReplicates();
			}
		});
		
		
	}
}
