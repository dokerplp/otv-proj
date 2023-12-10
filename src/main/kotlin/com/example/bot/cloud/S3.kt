package com.example.bot.cloud

import com.amazonaws.AmazonClientException
import com.amazonaws.AmazonServiceException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.services.s3.model.S3Object
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.*

@Slf4j
object S3 {
    private val logger: Logger = LoggerFactory.getLogger(S3::class.java)

    private val credentials: AWSCredentials = initCredentials()
    private val s3: AmazonS3 = initS3Client()

    private const val defaultBucketName = "otv-lab4-storage"

    private fun initCredentials(): AWSCredentials {
        try {
            return ProfileCredentialsProvider().credentials
        } catch (e: Exception) {
            throw AmazonClientException(
                "Cannot load the credentials from the credential profiles file. " +
                        "Please make sure that your credentials file is at the correct " +
                        "location (~/.aws/credentials), and is in valid format.", e
            )
        }
    }

    private fun initS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    "storage.yandexcloud.net", "ru-central1"
                )
            )
            .build()
    }

    fun createBucket(bucketName: String): Optional<Unit>  {
        return safeS3Action { s3.createBucket(bucketName) }
    }

    fun deleteBucket(bucketName: String): Optional<Unit>  {
        return safeS3Action { s3.deleteBucket(bucketName) }
    }

    fun listAllBuckets(): Optional<List<String>> {
        return safeS3Action {  s3.listBuckets().map { it.name } }
    }

    fun putObject(bucketName: String, file: File, format: String): Optional<String> {
        val fileKey = UUID.randomUUID().toString() + format
        val res = safeS3Action { s3.putObject(PutObjectRequest(bucketName, fileKey, file)) }
        return res.map { fileKey }
    }

    fun putObject(file: File, format: String): Optional<String> = putObject(defaultBucketName, file, format)

    fun getObject(bucketName: String, fileName: String): Optional<S3Object> {
        return safeS3Action { s3.getObject(GetObjectRequest(bucketName, fileName)) }
    }

    fun getObject(fileName: String): Optional<S3Object> = getObject(defaultBucketName, fileName)

    fun getObjects(fileNames: List<String>): List<S3Object> {
        return fileNames.map { getObject(it) }
            .filter { it.isPresent }
            .map { it.get() }
    }

    fun deleteObject(bucketName: String, fileName: String): Optional<Unit> {
        return safeS3Action { s3.deleteObject(bucketName, fileName) }
    }

    private fun <T : Any> safeS3Action(action: () -> T): Optional<T> {
        try {
            return Optional.of(action.invoke())
        } catch (ase: AmazonServiceException) {
            logger.error("""
                Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.
                Error Message:    ${ase.message}
                HTTP Status Code: ${ase.statusCode}
                AWS Error Code:   ${ase.errorCode}
                Error Type:       ${ase.errorType}
                Request ID:       ${ase.requestId}
            """.trimIndent())
        } catch (ace: AmazonClientException) {
            logger.error("""
                Caught an AmazonClientException, which means the client encountered a serious internal problem while trying to communicate with S3, such as not being able to access the network.
                Error Message: ${ace.message}
            """.trimIndent())
        }
        return Optional.empty()
    }
}