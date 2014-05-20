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

import es.uvigo.ei.sing.mla.daos.ExperimentDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ExperimentTest {
	@Autowired
	private ExperimentDAO dao;

	@Test
	public void testExperimentIsOnPlate() {
		assertThat(dao.get(1).isOnPlate()).isFalse();
	}

	@Test
	public void testExperimentIsMetadataComplete() {
		assertThat(dao.get(1).isMetadataComplete()).isFalse();
		assertThat(dao.get(2).isMetadataComplete()).isTrue();
	}

	@Test
	public void testExperimentCanCountReplicates() {
		assertThat(dao.get(1).countReplicates()).isEqualTo(4);
	}

	@Test
	public void testExperimentCanGetReplicates() {
		assertThat(dao.get(1).getReplicates().size()).isEqualTo(4);
	}

	@Test
	public void testExperimentCanGetReplicatesGivenPlate() {
		assertThat(dao.get(1).getReplicates(3).size()).isEqualTo(1);
	}

	@Test(expected = NullPointerException.class)
	public void testExperimentAddNullConditionThrowsException() {
		dao.get(1).addCondition(null);
	}

	@Test
	public void testExperimentCanAddCondition() {
		Experiment experiment = dao.get(1);

		ConditionGroup condition = new ConditionGroup();

		experiment.addCondition(condition);

		assertThat(experiment.getConditions().contains(condition));
	}

	@Test(expected = NullPointerException.class)
	public void testExperimentRemoveNullConditionThrowsException() {
		dao.get(1).removeCondition(null);
	}

	@Test
	public void testExperimentCanRemoveCondition() {
		Experiment experiment = dao.get(1);

		ConditionGroup condition = experiment.getConditions().get(0);
		assertThat(experiment.removeCondition(condition)).isTrue();

		condition = new ConditionGroup();
		assertThat(experiment.removeCondition(condition)).isFalse();
	}
}
