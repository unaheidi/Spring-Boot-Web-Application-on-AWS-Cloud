/**
 * rohan magare, 001231457, magare.r@husky.neu.edu
 * ritesh gupta, 001280361, gupta.rite@husky.neu.edu
 * pratiksha shetty, 00121643697, shetty.pr@husky.neu.edu
 * yogita jain, 001643815, jain.yo@husky.neu.edu
 **/
package com.csye6225.demo.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.csye6225.demo.entity.MediaFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;

@Service
public class FileArchiveService {

    private static final String S3_BUCKET_NAME = "ec2.csye6225-fall2017-guptarite.me.csye6225.com";


    @Autowired
    private AmazonS3 s3Client;

    /*@Value("${jsa.aws.access_key_id}")
    private String awsId;

    @Value("${jsa.aws.secret_access_key}")
    private String awsKey;

    @Value("${jsa.s3.region}")
   private String region;
*/

    /**
     * Save image to S3 and return MediaFile containing key and public URL
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public MediaFile saveFileToS3(MultipartFile multipartFile) throws FileArchiveServiceException, IOException {

        try {
            //BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
//            s3Client = AmazonS3ClientBuilder.standard()
//                    .withRegion(Regions.fromName(region))
//                    //.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                    .build();
            s3Client = new AmazonS3Client(DefaultAWSCredentialsProviderChain.getInstance());
            //s3Client = new AmazonS3Client(new ProfileCredentialsProvider(DefaultAWSCredentialsProviderChain.getInstance()));
            InputStream is = multipartFile.getInputStream();
            String key = Instant.now().getEpochSecond() + "_" + multipartFile.getName();
            String fileName = multipartFile.getOriginalFilename();
            /* save file */
            s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, key, is, new ObjectMetadata()));
            URL signedUrl = s3Client.getUrl(S3_BUCKET_NAME, key);
            return new MediaFile(key, signedUrl.toString(), fileName.toString());
        } catch (FileArchiveServiceException ex) {
            throw new FileArchiveServiceException("An error occurred saving file to S3", ex);
        }
    }


    /**
     * Delete image from S3 using specified key
     *
     * @param mediaFile
     */
    public void deleteFileFromS3(MediaFile mediaFile) {

       /* BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();*/

       s3Client = new AmazonS3Client(new ProfileCredentialsProvider());

        s3Client.deleteObject(new DeleteObjectRequest(S3_BUCKET_NAME, mediaFile.getKey()));
    }
}
