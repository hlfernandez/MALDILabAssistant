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

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;
import es.uvigo.ei.sing.mla.model.entities.Experiment;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ConditionGroupDAOTest {
	@Autowired
	private ConditionGroupDAO conditionDAO;

	@Autowired
	private ExperimentDAO experimentDAO;

	@Test
	public void testConditionGroupDAOCanAdd() {
		ConditionGroup condition = new ConditionGroup();

		conditionDAO.add(condition);

		assertThat(conditionDAO.get(condition.getId())).isNotNull();
	}

	@Test
	public void testConditionGroupDAOCanGet() {
		assertThat(conditionDAO.get(1)).isNotNull();
	}

	@Test
	public void testConditionGroupDAOCanReload() {
		ConditionGroup condition = conditionDAO.get(1);
		condition.setName("New Condition 1");

		conditionDAO.reload(condition);

		assertThat(conditionDAO.get(condition.getId()).getName()).isEqualTo(
				"Condition 1");
	}

	@Test
	public void testConditionGroupDAOCanUpdate() {
		ConditionGroup condition = conditionDAO.get(1);
		condition.setName("New Condition 1");

		conditionDAO.update(condition);

		assertThat(conditionDAO.get(condition.getId()).getName()).isEqualTo(
				"New Condition 1");
	}

	@Test
	public void testConditionGroupDAOCanDelete() {
		ConditionGroup condition = conditionDAO.get(1);

		conditionDAO.delete(condition);

		assertThat(conditionDAO.get(1)).isNull();
	}

	@Test
	public void testConditionGroupDAOCanList() {
		Experiment experiment = experimentDAO.get(1);

		assertThat(conditionDAO.list(experiment).size()).isEqualTo(1);
	}
}
