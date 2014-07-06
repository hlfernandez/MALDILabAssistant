package es.uvigo.ei.sing.mla.view.models.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import es.uvigo.ei.sing.mla.services.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("file:src/test/resources/META-INF/context.xml")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionDbUnitTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("file:src/test/resources/META-INF/dataset.xml")
public class BasicOutputSorterTest {
	@Autowired
	ExperimentService service;

	@Autowired
	OutputSorter sorter;

	private File tmpDir;

	@Before
	public void createTmpDir() throws IOException {
		tmpDir = new File("src/test/resources/tmp");
		FileUtils.cleanDirectory(tmpDir);
	}
	
	@Before
	public void cleanTmpDir() throws IOException {
		FileUtils.cleanDirectory(tmpDir);
	}

	@Test
	public void testBasicOutputSorterCanSort() throws IOException {
		final File datasetDir = new File("src/test/resources/dataset");
		final File exampleDir = new File("src/test/resources/exp1");

		sorter.sort(service.get(3), datasetDir, "[Condition]/[Sample]/[Replicate]", tmpDir);

		final File[] datasetFiles = exampleDir.listFiles();
		final File[] tmpFiles = tmpDir.listFiles();
		Arrays.sort(datasetFiles);
		Arrays.sort(tmpFiles);
		
		for (int i = 0; i < datasetFiles.length; ++i) {
			assertThat(datasetFiles[i].getName()).isEqualTo(tmpFiles[i].getName());
		}
	}

	@Test
	public void testBasicOutputSorterCanCheckPath() {
		assertThat(sorter.checkPath("[Replicate]")).isTrue();
		assertThat(sorter.checkPath("[Sample]/[Replicate]")).isTrue();
		assertThat(sorter.checkPath("[Condition]/[Replicate]")).isTrue();
		assertThat(sorter.checkPath("[Condition]/[Sample]/[Replicate]")).isTrue();

		assertThat(sorter.checkPath("/[Replicate]")).isFalse();
		assertThat(sorter.checkPath("[Condition]/[Sample]")).isFalse();
		assertThat(sorter.checkPath("[Condition]/[Replicate]/[Sample]")).isFalse();
		assertThat(sorter.checkPath("/[Condition]/[Sample]/[Replicate]")).isFalse();
	}
}
