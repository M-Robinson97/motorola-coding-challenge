package com.test_apps.motorola_coding_challenge.integration_test;

import com.test_apps.motorola_coding_challenge.MotorolaCodingChallengeApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MotorolaCodingChallengeApplication.class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class FileManagerController_BaseIT {

    @LocalServerPort
    protected int portNumber;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseUrl;
    protected static Path tmpDir;

    @BeforeAll
    protected void setUp() {
        baseUrl = "http://localhost:" + portNumber + "/fileManager";
        System.setProperty("storage.root", tmpDir.toString());
    }

    @AfterAll
    protected void tearDown() throws IOException{
        FileSystemUtils.deleteRecursively(tmpDir);
    }

    protected void writeTestTextFile(String relativePath, String content) throws Exception {
        final File toWrite = new File(tmpDir.toFile(), relativePath);
        boolean isSuccess = toWrite.getParentFile().mkdirs();
        if(!isSuccess) throw new Exception("Could not create directories");
        try(FileWriter writer = new FileWriter(toWrite)) {
            writer.write(content);
        }
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) throws IOException{
        tmpDir = Files.createTempDirectory("file-manager-test");
        registry.add("storage.root", tmpDir::toString);
    }
}
