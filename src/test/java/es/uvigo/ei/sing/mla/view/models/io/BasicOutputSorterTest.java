package es.uvigo.ei.sing.mla.view.models.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;

public class BasicOutputSorterTest {
	@Autowired
	BasicOutputSorter bos;
	
	@Rule
	private TemporaryFolder folder = new TemporaryFolder();
	
	@Before
	private void createTestData() throws IOException {
		File sub1 = folder.newFolder("1");
		File sub2 = folder.newFolder("2");
		
		new File(sub1.getPath() + "A1.csv");
		new File(sub2.getPath() + "B2.csv");
	}
	
	@Test
	public void testBasicOutputSorterCanSort() {
		
	}
	
	@Test
	public void testBasicOutputSorterCanCheckPath() {
		assertThat(bos.checkPath("[Replicate]")).isTrue();
		assertThat(bos.checkPath("[Sample]/[Replicate]")).isTrue();
		assertThat(bos.checkPath("[Condition]/[Replicate]")).isTrue();
		assertThat(bos.checkPath("[Condition]/[Sample]/[Replicate]")).isTrue();
		
		assertThat(bos.checkPath("/[Replicate]")).isFalse();
		assertThat(bos.checkPath("[Condition]/[Sample]")).isFalse();
		assertThat(bos.checkPath("[Condition]/[Replicate]/[Sample]")).isFalse();
		assertThat(bos.checkPath("/[Condition]/[Sample]/[Replicate]")).isFalse();
	}
}
