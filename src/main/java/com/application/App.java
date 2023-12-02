package com.application;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;

import java.util.List;
public class App {
    private static AmazonS3 s3;
  
    public static void main(String[] args){
        if(args.length<2){
            System.out.format("Usage: <bucket-name to create>, < AWS region to use> \n"+ "Example: first-bucket us-east-2");
            return;
        }
        String bucket_name = args[0];
        String aws_region = args[1];
        s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
                .withRegion(aws_region)
                .build();
                
        //List all the current buckets
        ListBuckets();
        
        if(s3.doesBucketExistV2(bucket_name)){
            System.out.format("Cannot create the bucket \n"+"A bucket named %s exists",bucket_name);
            return;
        }
        else{
            try{
                System.out.format("Creating a new bucket named %s", bucket_name);
                s3.createBucket(new CreateBucketRequest(bucket_name, aws_region));
            }
            catch (AmazonS3Exception e) {
            System.err.println(e.getErrorMessage());
        }
        }
    
        //Deleting the just created bucket;
        try{
            System.out.printf("Deleting the bucket named %s",bucket_name);
            s3.deleteBucket(bucket_name);
        }
        catch(AmazonS3Exception e){
            //System.err.println(e.getErrorMessage());
        }
        //Listing all buckets now.
        ListBuckets();
        
    }
      private static void ListBuckets(){
        List<Bucket> buckets = s3.listBuckets();
        System.out.format("My buckets now are");
        for(Bucket b: buckets){
            System.out.println(b.getName());
        }
    }
  
}

