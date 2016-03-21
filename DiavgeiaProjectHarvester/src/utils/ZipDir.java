package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDir {
	
	/**
	 * Zip the provided directory as is (keep hierarchies)
	 */
	public void zipFolder(String srcDirectory, String zipFile) {
		
		File srcFile = new File(srcDirectory);
		
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			addDirToArchive(zos, srcFile, srcDirectory);
			
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void addDirToArchive(ZipOutputStream zos, File srcFile, String srcDir) {
		
		System.out.println("\nZipping directory: " + srcDir);
		
		File[] files = srcFile.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			
			if (files[i].isDirectory()) { //if it is directory, use recursion
				addDirToArchive(zos, files[i], srcDir);
				continue;
			}
			
			try {
				System.out.println("Adding file: " + files[i].getName());
				
				byte[] buffer = new byte[1024];
				FileInputStream fis = new FileInputStream(files[i]);
				
				if ( !formatPath(srcFile.toString()).replace(srcDir, "").equals(formatPath(srcFile.toString())) ) {
					zos.putNextEntry( new ZipEntry(formatPath(srcFile.toString()).replace(srcDir, "") + "/" + files[i].getName()) );
				} else {
					zos.putNextEntry( new ZipEntry(files[i].getName()) );
				}
				
				int length;

				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}

				zos.closeEntry();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	private static String formatPath(String inputPath) {
		
		String formattedPath = inputPath.replace("\\", "/");
		
		return formattedPath;
	}

}