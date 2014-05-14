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

import es.uvigo.ei.sing.mla.daos.ConditionGroupDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ConditionGroupTest {
	@Autowired
	private ConditionGroupDAO dao;

	@Test
	public void testConditionIsOnPlate() {
		assertThat(dao.get(1).isOnPlate()).isFalse();
		assertThat(dao.get(2).isOnPlate()).isTrue();
	}

	@Test
	public void testConditionCanCountReplicates() {
		assertThat(dao.get(1).countReplicates()).isEqualTo(4);
	}

	@Test(expected = NullPointerException.class)
	public void testConditionAddNullSampleThrowsException() {
		dao.get(1).addSample(null);
	}

	@Test
	public void testConditionCanAddCondition() {
		ConditionGroup condition = dao.get(1);

		Sample sample = new Sample();

		condition.addSample(sample);

		assertThat(condition.getSamples().contains(sample));
	}

	@Test(expected = NullPointerException.class)
	public void testConditionRemoveNullSampleThrowsException() {
		dao.get(1).removeSample(null);
	}

	@Test
	public void testConditionCanRemoveCondition() {
		ConditionGroup condition = dao.get(1);

		Sample sample = condition.getSamples().get(0);
		assertThat(condition.removeSample(sample)).isTrue();

		sample = new Sample();
		assertThat(condition.removeSample(sample)).isFalse();
	}
}
