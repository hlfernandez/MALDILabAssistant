package es.uvigo.ei.sing.mla.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.zkoss.util.media.Media;

public class DataPackager {
	public static void unpackageData(Media media, File outputDirectory)
			throws InvalidFormatException {
		if (media.isBinary()) {
			File file = new File(media.getName());

			switch (FilenameUtils.getExtension(media.getName())) {
			case "zip":
				try {
					FileUtils.writeByteArrayToFile(file, media.getByteData());
					ZipFile zipfile = new ZipFile(file);

					for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e
							.hasMoreElements();) {
						ZipEntry entry = (ZipEntry) e.nextElement();
						unzipEntry(zipfile, entry, outputDirectory);
					}

				} catch (IOException e1) {
				}

				break;

			case "tar":
				try {
					FileUtils.writeByteArrayToFile(file, media.getByteData());
					try (TarArchiveInputStream myTarFile = new TarArchiveInputStream(
							new FileInputStream(file))) {
						TarArchiveEntry entry = null;

						while ((entry = myTarFile.getNextTarEntry()) != null) {
							if (entry.isDirectory()) {
								createDirectory(new File(outputDirectory,
										entry.getName()));
							} else {
								byte[] content = new byte[(int) entry.getSize()];
								myTarFile.read(content, 0, content.length);

								File entryFile = new File(outputDirectory,
										entry.getName());
								try (FileOutputStream outputFile = new FileOutputStream(
										entryFile)) {
									IOUtils.write(content, outputFile);
								}
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				break;

			default:
				throw new InvalidFormatException("");
			}
		}
	}

	private static void unzipEntry(ZipFile zipfile, ZipEntry entry,
			File outputDirectory) throws IOException {
		if (entry.isDirectory()) {
			createDirectory(new File(outputDirectory, entry.getName()));
			return;
		}

		File outputFile = new File(outputDirectory, entry.getName());

		if (!outputFile.getParentFile().exists()) {
			createDirectory(outputFile.getParentFile());
		}

		BufferedInputStream inputStream = new BufferedInputStream(
				zipfile.getInputStream(entry));
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(outputFile));

		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}

	public static File zipData(File inputDir, String fileName) {
		File file = new File("tmp.zip");

		try (ArchiveOutputStream zipStream = new ArchiveStreamFactory()
		.createArchiveOutputStream(ArchiveStreamFactory.ZIP,
				new FileOutputStream(file))) {

			zipEntry(zipStream, inputDir.listFiles());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return file;
	}

	private static void zipEntry(ArchiveOutputStream zipStream, File[] files)
			throws Exception {
		for (File file : files) {
			zipStream.putArchiveEntry(new ZipArchiveEntry(file, file.getName()));

			if (!file.isDirectory()) {
				IOUtils.copy(new FileInputStream(file), zipStream);
			} else {
				zipEntry(zipStream, file.listFiles());
			}

			zipStream.closeArchiveEntry();
		}
	}

	private static void createDirectory(File dir) {
		if (!dir.mkdirs()) {
			throw new RuntimeException("Can not create dir " + dir);
		}
	}
}
