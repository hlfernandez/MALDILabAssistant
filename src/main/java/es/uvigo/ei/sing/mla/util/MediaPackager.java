package es.uvigo.ei.sing.mla.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

			switch (extension) {
			case "zip":
				File file = new File(media.getName());

				try {
					FileUtils.writeByteArrayToFile(file, media.getByteData());
					ZipFile zipfile = new ZipFile(file);

					for (Enumeration<? extends ZipEntry>  e = zipfile.entries(); e.hasMoreElements();) {
						ZipEntry entry = (ZipEntry) e.nextElement();
						unzipEntry(zipfile, entry, outputDirectory);
					}

				} catch (IOException e1) {
				}

				break;

			case "tar":

			default:
				throw new InvalidFormatException("");
			}
		}
	}

	private static void unzipEntry(ZipFile zipfile, ZipEntry entry,
			File outputDir) throws IOException {
		if (entry.isDirectory()) {
			createDirectory(new File(outputDir, entry.getName()));
			return;
		}

		File outputFile = new File(outputDir, entry.getName());

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
