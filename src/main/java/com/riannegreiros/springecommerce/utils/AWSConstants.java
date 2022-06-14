package com.riannegreiros.springecommerce.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AWSConstants {
    public static String REGION;

    @Value("${aws.region}")
    public void setRegion(String region) {
        REGION = region;
    }

    public static String ACCESS_KEY;

    @Value("${aws.access-key}")
    public void setAccessKey(String accessKey) {
        ACCESS_KEY = accessKey;
    }

    public static String SECRET_KEY;

    @Value("${aws.secret-key}")
    public void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static String BUCKET_NAME;

    @Value("${aws.bucket-name}")
    public void setBucketName(String bucketName) {
        BUCKET_NAME = bucketName;
    }
}
