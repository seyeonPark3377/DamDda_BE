    package org.eightbit.damdda.common.utils.cloud;

    import com.amazonaws.HttpMethod;
    import com.amazonaws.services.s3.AmazonS3;
    import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
    import lombok.NoArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Component;

    import java.util.Date;
    import java.io.File;

    @Component
    @NoArgsConstructor
    public class S3Util {

        private AmazonS3 amazonS3;

        @Value("${cloud.aws.credentials.bucket}")
        private String bucketName;

        public S3Util(AmazonS3 amazonS3) {
            this.amazonS3 = amazonS3;
        }

        public void uploadFileToS3(String fileName, String fileType, File file) {
            amazonS3.putObject(bucketName, fileName + fileType, file);
        }

        public String generatePresignedUrlWithExpiration(String fileName, String fileType, int expirationInMinutes) {
            Date expiration = new Date(System.currentTimeMillis() + 1000L * 60 * expirationInMinutes);
            return amazonS3.generatePresignedUrl(
                    new GeneratePresignedUrlRequest(bucketName, fileName + fileType, HttpMethod.GET)
                            .withExpiration(expiration)
            ).toString();
        }

    }