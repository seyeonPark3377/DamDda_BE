package org.eightbit.damdda.project.config;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NcpConfig {
    @Value("${CLOUD_AWS_ACCESS_KEY_MINHEE}")
    private String accessKey;

    @Value("${CLOUD_AWS_SECRET_KEY_MINHEE}")
    private String secretKey;

    @Value("${CLOUD_AWS_REGION}")
    private String region;

    @Value("${CLOUD_AWS_S3_ENDPOINT}")
    private String endPoint;

    @Bean
    public AmazonS3 AmazonS3(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

}
