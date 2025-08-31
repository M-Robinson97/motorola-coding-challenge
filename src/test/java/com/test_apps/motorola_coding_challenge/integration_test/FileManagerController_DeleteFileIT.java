package com.test_apps.motorola_coding_challenge.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileManagerController_DeleteFileIT extends FileManagerController_BaseIT {

    @Test
    void shouldDeleteFile_Success() throws Exception {
        // Arrange
        final String fileName = "file.txt";
        final String relativePath = "/dir/nestedDir/" + fileName;
        final String fileContent = "I am a teapot";
        final String fileUrl = baseUrl + relativePath;

        writeTestTextFile(relativePath, fileContent);

        // Act
        // 'restTemplate.delete' is void - use 'exchange' instead to check response code
        ResponseEntity<Void> response = restTemplate.exchange(
                fileUrl,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void shouldDeleteFile_FailsNoSuchFile() {
        // Arrange
        final String fileName = "file.txt";
        final String relativePath = "/dir/nestedDir/" + fileName;
        final String fileUrl = baseUrl + relativePath;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                fileUrl,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteFile_FailWithBadRequestForAbsolutePath() {
        // Arrange
        final String relativePath = "/C:/deletingyourimportantsystemfiles.txt";
        final String fileUrl = baseUrl + relativePath;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                fileUrl,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldDeleteFile_FailWithBadRequestForGettingParentDirectory() {
        // Arrange
        final String relativePath = "/../../deletingyourdreamjobapplication.txt";
        final String fileUrl = baseUrl + relativePath;

        // Act
        ResponseEntity<Void> response = restTemplate.exchange(
                fileUrl,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Assert
        assertNotNull(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
