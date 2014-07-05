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

import es.uvigo.ei.sing.mla.model.entities.Sample;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class SampleDAOTest {
	@Autowired
	private SampleDAO sampleDAO;

	@Autowired
	private ConditionGroupDAO conditionDAO;

	@Test
	public void testSampleDAOCanAdd() {
		Sample sample = new Sample();

		sampleDAO.add(sample);

		assertThat(sampleDAO.get(sample.getId())).isNotNull();
	}

	@Test
	public void testSampleDAOCanGet() {
		assertThat(sampleDAO.get(1)).isNotNull();
	}

	@Test
	public void testSampleDAOCanReload() {
		Sample sample = sampleDAO.get(1);
		sample.setName("New Sample 1");

		sampleDAO.reload(sample);

		assertThat(sampleDAO.get(sample.getId()).getName()).isEqualTo(
				"Sample 1");
	}

	@Test
	public void testSampleDAOCanUpdate() {
		Sample sample = sampleDAO.get(1);
		sample.setName("New Sample 1");

		sampleDAO.update(sample);

		assertThat(sampleDAO.get(sample.getId()).getName()).isEqualTo(
				"New Sample 1");
	}

	@Test
	public void testSampleDAOCanDelete() {
		Sample sample = sampleDAO.get(1);

		sampleDAO.delete(sample);

		assertThat(sampleDAO.get(1)).isNull();
	}
}
