package com.test_apps.motorola_coding_challenge.integration_test;

import com.test_apps.motorola_coding_challenge.model.FileListDto;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileManagerController_ListFilesIT extends FileManagerController_BaseIT {

    @Test
    void shouldListFiles_Success() throws Exception {
        final String fileName1 = "/dir1/file1.txt";
        final String fileName2 = "/dir2/dir3/file2.txt";
        final String fileName3 = "/file3.txt";

        final String fileContent1 = "content1";
        final String fileContent2 = "content2";
        final String fileContent3 = "content3";

        writeTestTextFile(fileName1, fileContent1);
        writeTestTextFile(fileName2, fileContent2);
        writeTestTextFile(fileName3, fileContent3);

        ResponseEntity<FileListDto> response =
                restTemplate.getForEntity(baseUrl, FileListDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getFileNames());
        List<String> fileNames = response.getBody().getFileNames();
        assertThat(fileNames).containsExactlyInAnyOrder(
                "dir1\\file1.txt",
                "dir2\\dir3\\file2.txt",
                "file3.txt"
        );
    }

    @Test
    void shouldListFiles_SuccessEmptyList() throws IOException {
        // Teardown means we need to create root dir manually if not creating a file
        Files.createDirectories(tmpDir);
        ResponseEntity<FileListDto> response =
                restTemplate.getForEntity(baseUrl, FileListDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getFileNames());
        List<String> fileNames = response.getBody().getFileNames();
        assertThat(fileNames).isEmpty();
    }
}
