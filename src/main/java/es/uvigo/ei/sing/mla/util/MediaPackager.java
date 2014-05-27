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

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.zkoss.util.media.Media;

public class MediaPackager {
	public static void unpackageMedia(Media media, File outputDirectory)
			throws InvalidFormatException {
		if (media.isBinary()) {
			String extension = media
					.getName()
					.toLowerCase()
					.substring(
							media.getName().toLowerCase().lastIndexOf(".") + 1);

			File file = new File(media.getName());

			switch (extension) {
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
					TarArchiveInputStream tais = new TarArchiveInputStream(
							new FileInputStream(file));

					TarArchiveEntry entry;

					while ((entry = tais.getNextTarEntry()) != null) {
						untarEntry(entry, outputDirectory);
					}

				} catch (IOException e1) {
				}

				break;

			default:
				throw new InvalidFormatException("");
			}
		}
	}

	private static void untarEntry(TarArchiveEntry entry, File outputDirectory) {
		if (entry.isDirectory()) {
			createDirectory(new File(outputDirectory, entry.getName()));
			return;
		}

		File outputFile = new File(outputDirectory, entry.getName());

		if (!outputFile.getParentFile().exists()) {
			createDirectory(outputFile.getParentFile());
		}

		try (BufferedInputStream inputStream = new BufferedInputStream(
				new FileInputStream(entry.getFile()));
				BufferedOutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(outputFile))) {

			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {

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

	private static void createDirectory(File dir) {
		if (!dir.mkdirs()) {
			throw new RuntimeException("Can not create dir " + dir);
		}
	}
}
