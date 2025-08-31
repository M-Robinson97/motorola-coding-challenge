package com.test_apps.motorola_coding_challenge.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileManagerController_UploadFileIT extends FileManagerController_BaseIT {

    @Test
    void shouldUploadFile_Success() {
        final String fileName = "file.txt";
        final String relativePath = "/dir/nestedDir/" + fileName;
        final String fileContent = "I am a teapot";
        final String fileUrl = baseUrl + relativePath;
        final String expectedFileName = "dir/nestedDir/" + fileName;

        HttpEntity<MultiValueMap<String, Object>> testFile = getTestHttpEntity(relativePath, fileContent, fileUrl);
        ResponseEntity<String> response = restTemplate.postForEntity(fileUrl, testFile, String.class);

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response.getBody());
        assertThat(response.getBody()).isEqualTo(expectedFileName);
    }

    @Test
    void shouldUploadFile_SuccessForOverwriting() {
        final String fileName = "file.txt";
        final String relativePath = "/dir/nestedDir/" + fileName;
        final String fileContent1 = "I am a teapot";
        final String fileContent2 = "I am a pot of coffee";
        final String fileUrl = baseUrl + relativePath;
        final String expectedFileName = "dir/nestedDir/" + fileName;

        HttpEntity<MultiValueMap<String, Object>> testFile1 = getTestHttpEntity(relativePath, fileContent1, fileUrl);
        HttpEntity<MultiValueMap<String, Object>> testFile2 = getTestHttpEntity(relativePath, fileContent2, fileUrl);

        ResponseEntity<String> response1 = restTemplate.postForEntity(fileUrl, testFile1, String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity(fileUrl, testFile2, String.class);

        assertNotNull(response1);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response1.getBody());
        assertThat(response1.getBody()).isEqualTo(expectedFileName);
        assertNotNull(response2);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(response2.getBody());
        assertThat(response2.getBody()).isEqualTo(expectedFileName);
    }

    @Test
    void shouldUploadFile_FailWithBadRequestForAbsolutePath() {
        final String relativePath = "/C:/dir/nestedDir/stealingyoursecrets.txt";
        final String fileContent = "I am a teapot";
        final String fileUrl = baseUrl + relativePath;

        HttpEntity<MultiValueMap<String, Object>> testFile = getTestHttpEntity(relativePath, fileContent, fileUrl);
        ResponseEntity<String> response = restTemplate.postForEntity(fileUrl, testFile, String.class);

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUploadFile_FailWithBadRequestForGettingParentDirectory() {
        final String relativePath = "/../../definitelystealingyoursecretsnow.txt";
        final String fileContent = "I am a teapot";
        final String fileUrl = baseUrl + relativePath;

        HttpEntity<MultiValueMap<String, Object>> testFile = getTestHttpEntity(relativePath, fileContent, fileUrl);
        ResponseEntity<String> response = restTemplate.postForEntity(fileUrl, testFile, String.class);

        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

}
