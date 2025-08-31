package com.test_apps.motorola_coding_challenge.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileManagerController_GetFileIT extends FileManagerController_BaseIT {

    @Test
    void shouldGetFile_Success() throws Exception {
        // Arrange
        final String fileName = "file.txt";
        final String relativePath = "/dir/nestedDir/" + fileName;
        final String fileContent = "I am a teapot";
        final String fileUrl = baseUrl + relativePath;

        writeTestTextFile(relativePath, fileContent);

        // Act
        ResponseEntity<Resource> response = restTemplate.getForEntity(fileUrl, Resource.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        Resource resource = response.getBody();
        assertThat(resource.getFilename()).isEqualTo(fileName);
        String resultFileContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(resultFileContent).isEqualTo(fileContent);
    }

    @Test
    void shouldGetFile_FailWithBadRequestForAbsolutePath() {
        // Arrange
        final String relativePath = "/C:/tellmeallyoursecrets.txt";
        final String badFileUrl = baseUrl + relativePath;

        // Act
        ResponseEntity<Resource> response = restTemplate.getForEntity(badFileUrl, Resource.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetFile_FailWithBadRequestForGettingParentDirectory() {
        // Arrange
        final String relativePath = "/../../tellmeallyoursecrets.txt";
        final String badFileUrl = baseUrl + relativePath;

        // Act
        ResponseEntity<Resource> response = restTemplate.getForEntity(badFileUrl, Resource.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
