package com.test_apps.motorola_coding_challenge.integration_test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileManagerController_BaseIT {

    @LocalServerPort
    protected int portNumber;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseUrl;
    protected Path tmpDir;

    @BeforeAll
    protected void setUp() throws IOException {
        baseUrl = "http://localhost:" + portNumber + "/fileManager";
        tmpDir = Files.createTempDirectory("file-manager-test");
        System.setProperty("storage.root", tmpDir.toString());
    }

    @AfterAll
    protected void tearDown() throws IOException{
        FileSystemUtils.deleteRecursively(tmpDir);
    }
}
