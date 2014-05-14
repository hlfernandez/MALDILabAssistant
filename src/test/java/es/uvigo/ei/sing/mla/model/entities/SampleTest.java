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

import es.uvigo.ei.sing.mla.daos.SampleDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class SampleTest {
	@Autowired
	private SampleDAO dao;

	@Test
	public void testSampleIsOnPlate() {
		assertThat(dao.get(1).isOnPlate()).isFalse();
		assertThat(dao.get(2).isOnPlate()).isTrue();
	}

	@Test
	public void testSampleCanCountReplicates() {
		assertThat(dao.get(1).countReplicates()).isEqualTo(2);
	}

	@Test(expected = NullPointerException.class)
	public void testSampleAddNullReplicateThrowsException() {
		dao.get(1).addReplicate(null);
	}

	@Test
	public void testSampleCanAddCondition() {
		Sample sample = dao.get(1);

		Replicate replicate = new Replicate();

		sample.addReplicate(replicate);

		assertThat(sample.getReplicates().contains(replicate));
	}

	@Test(expected = NullPointerException.class)
	public void testSampleRemoveNullReplicateThrowsException() {
		dao.get(1).removeReplicate(null);
	}

	@Test
	public void testSampleCanRemoveCondition() {
		Sample sample = dao.get(1);

		Replicate replicate = sample.getReplicates().get(0);
		assertThat(sample.removeReplicate(replicate)).isTrue();

		replicate = new Replicate();
		assertThat(sample.removeReplicate(replicate)).isFalse();
	}
}
