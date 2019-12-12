package com.kxtract.s3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
