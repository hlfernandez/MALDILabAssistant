package es.uvigo.ei.sing.mla.daos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import es.uvigo.ei.sing.mla.model.entities.Replicate;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/daos.xml")
@TestExecutionListeners({ 
	DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class ReplicateDAOTest {
	@Autowired
	private ReplicateDAO dao;

	private List<Replicate> replicates;

	@Before
	public void populateReplicates() {
		replicates = new ArrayList<>();
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
	}

	@Test
	public void testReplicateDAOCanAdd() {
		Replicate replicate = new Replicate();
		dao.add(replicate);

		assertThat(dao.get(replicate.getId())).isNotNull();
	}
}
