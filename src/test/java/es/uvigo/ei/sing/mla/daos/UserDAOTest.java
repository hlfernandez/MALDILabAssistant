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

import es.uvigo.ei.sing.mla.model.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/daos.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class UserDAOTest {
	@Autowired
	private UserDAO userDAO;

	@Test
	public void testSampleDAOCanAdd() {
		User user = new User("manolo", "manolo");

		userDAO.add(user);

		assertThat(userDAO.get(user.getLogin())).isNotNull();
	}

	@Test
	public void testSampleDAOCanGet() {
		assertThat(userDAO.get("pepe")).isNotNull();
	}

	@Test
	public void testSampleDAOCanReload() {
		User user = userDAO.get("pepe");
		user.setPassword("New Pepe Password");

		userDAO.reload(user);

		assertThat(userDAO.get(user.getLogin()).getPassword())
				.isEqualTo("pepe");
	}
}
