package es.uvigo.ei.sing.mla.daos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.uvigo.ei.sing.mla.model.entities.Replicate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/test/resources/META-INF/daos.xml")
public class ReplicateDAOTest {

	@Autowired
	private ApplicationContext applicationContext;

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Autowired
	private ReplicateDAO dao;

	private List<Replicate> replicates;

	@Before
	public void populateReplicates() {
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
		replicates.add(new Replicate());
	}

	@Before
	public void insertReplicates() {
		em.getTransaction().begin();

		for (final Replicate replicate : replicates) {
			if (replicate.getId() == null) {
				em.persist(replicate);
			} else {
				em.merge(replicate);
			}
		}

		em.getTransaction().commit();
	}

	@Test
	public void testReplicateDAOCanAdd() {
		Replicate replicate = new Replicate();
		dao.add(replicate);

		assertThat(dao.get(replicate.getId())).isNotNull();
	}
}
