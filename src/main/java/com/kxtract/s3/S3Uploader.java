package com.kxtract.s3;

import java.io.File;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3Uploader {

	public static boolean fileAlreadyExistsInS3(String bucketName, String fileName) {
        AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();      
        try {
            boolean exists = s3.doesObjectExist(bucketName, fileName);
            return exists;      
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
    }
	
	public static void uploadFileToS3(String bucketName, File f) {
		if (bucketName == null) {
			throw new IllegalArgumentException("bucketName cannot be null");
		}
		if (f == null) {
			throw new IllegalArgumentException("File cannot be null");
		}
		if (!f.exists()) {
			throw new IllegalArgumentException("File (" + f.getAbsolutePath() + ") does not exist");
		}
		AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();   
		PutObjectRequest p = new PutObjectRequest(bucketName, f.getName(), f);
		
		// Put Object
		s3.putObject(p);
	}
	
	public static String getS3Filename(String bucketName, String filename) {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		return s3.getUrl(bucketName, filename).toString();
	}

	/**
	 * For Testing only
	 */
	static void deleteFileFromS3(String bucketName, String fileName) {
		AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();      
        try {
            boolean exists = s3.doesObjectExist(bucketName, fileName);
			if (!exists) {
				throw new IllegalArgumentException(
						"That file (" + fileName + ") does not exist in S3 bucket (" + bucketName + ")");
			}
			s3.deleteObject(bucketName, fileName);
        } catch (Exception e) {
        	throw new RuntimeException(e);
        }
	}
}


