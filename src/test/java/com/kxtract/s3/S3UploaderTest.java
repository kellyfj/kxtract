package com.kxtract.s3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class S3UploaderTest {

	@Test
	public void testDoesObjectExist_NullBucket() {
		String bucketName = null;
		String fileName = null;
		
		assertThrows(RuntimeException.class,
	            ()->{S3Uploader.fileAlreadyExistsInS3(bucketName, fileName);} );		
	}
	
	
	@Test
	public void testDoesObjectExist_NullFilename() {
		String bucketName = "kxtract";
		String fileName = null;
		
		assertThrows(RuntimeException.class,
	            ()->{S3Uploader.fileAlreadyExistsInS3(bucketName, fileName);} );		
	}
	
	@Test
	public void testDoesObjectExist_NoSuchFile() {
		String bucketName = "kxtract";
		String fileName = "NoSuchObjectName.mp3";
		
		assertFalse(S3Uploader.fileAlreadyExistsInS3(bucketName, fileName));		
	}
	
	@Test
	public void testUploadFail_NullFile() {	
		String bucketName = null;
		File file = null;
		assertThrows(IllegalArgumentException.class,
				()->{S3Uploader.uploadFileToS3(bucketName, file);} );
	}
	
	@Test
	public void testUploadFail_NullBucket() {	
		String bucketName = null;
		File file = new File("/tmp/test.txt");
		assertThrows(IllegalArgumentException.class,
				()->{S3Uploader.uploadFileToS3(bucketName, file);} );
	}
		
	@Test
	public void testUploadFail_FileDoesNotExist() {	
		String bucketName = "kxtract";
		File file = new File("/tmp/test.txt");
		assertThrows(IllegalArgumentException.class,
				()->{S3Uploader.uploadFileToS3(bucketName, file);} );
	}

	@Test
	public void testUploadOK() throws URISyntaxException {			
		File resourcesDirectory = new File("src/test/resources");
		File file = new File(resourcesDirectory.getAbsolutePath() + "/testpodcasts.txt");
		String fileName = file.getName();
		String bucketName = "kxtract";
		
		//IF file already present THEN delete it
		if(S3Uploader.fileAlreadyExistsInS3(bucketName, fileName)) {
			S3Uploader.deleteFileFromS3(bucketName, fileName);
		}
		S3Uploader.uploadFileToS3(bucketName, file);
		
		assertTrue(S3Uploader.fileAlreadyExistsInS3(bucketName, fileName));
		
		//Cleanup after myself
		S3Uploader.deleteFileFromS3(bucketName, fileName);
	}
}

