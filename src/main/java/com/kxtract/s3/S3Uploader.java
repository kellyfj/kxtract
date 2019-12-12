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
		AmazonS3 s3 =  AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();   
		PutObjectRequest p = new PutObjectRequest(bucketName, f.getName(), f);
		
		// Put Object
		s3.putObject(p);
	}
}
