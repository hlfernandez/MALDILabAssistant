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

import es.uvigo.ei.sing.mla.model.entities.Replicate;
import es.uvigo.ei.sing.mla.model.entities.Sample;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ReplicateDAOTest {
	@Autowired
	private ReplicateDAO replicateDAO;

	@Autowired
	private SampleDAO sampleDAO;

	@Test
	public void testReplicateDAOCanAdd() {
		Replicate replicate = new Replicate();

		replicateDAO.add(replicate);

		assertThat(replicateDAO.get(replicate.getId())).isNotNull();
	}

	@Test
	public void testReplicateDAOCanGet() {
		assertThat(replicateDAO.get(1)).isNotNull();
	}

	@Test
	public void testReplicateDAOCanReload() {
		Replicate replicate = replicateDAO.get(1);
		replicate.setName("New Replicate 1");

		replicateDAO.reload(replicate);

		assertThat(replicateDAO.get(replicate.getId()).getName()).isEqualTo(
				"Replicate 1");
	}

	@Test
	public void testReplicateDAOCanUpdate() {
		Replicate replicate = replicateDAO.get(1);
		replicate.setName("New Replicate 1");

		replicateDAO.update(replicate);

		assertThat(replicateDAO.get(replicate.getId()).getName()).isEqualTo(
				"New Replicate 1");
	}

	@Test
	public void testReplicateDAOCanDelete() {
		Replicate replicate = replicateDAO.get(1);

		replicateDAO.delete(replicate);

		assertThat(replicateDAO.get(1)).isNull();
	}

	@Test
	public void testReplicateDAOCanList() {
		Sample sample = sampleDAO.get(1);

		assertThat(replicateDAO.list(sample).size()).isEqualTo(2);
	}
}
