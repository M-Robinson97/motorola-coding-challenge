package com.test_apps.motorola_coding_challenge.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileManagerController_UploadFileIT extends FileManagerController_BaseIT {

    @Test
    void shouldUploadFile() {
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
}
