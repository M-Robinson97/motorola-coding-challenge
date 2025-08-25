package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileRepositoryImplTest {
    @InjectMocks
    FileRepositoryImpl fileRepository;
    @Mock
    StorageService storageServiceMock;

    @Test
    void get_Succeeds() throws IOException {
        final String fileName = "FileRepositoryImplTest.java";
        URI fileUri = Path.of("src/test/java/com/test_apps/motorola_coding_challenge/repository/FileRepositoryImplTest.java").toUri();
        when(storageServiceMock.buildFilePathUri(fileName)).thenReturn(fileUri);

        // Act
        Resource result = fileRepository.get(fileName);

        //Assert
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.isReadable());
        assertEquals("FileRepositoryImplTest.java", Path.of(result.getURI()).getFileName().toString());
    }

    @Test
    void get_NoFileName() {
        final String fileName = "";

        // Act
        Resource result = fileRepository.get(fileName);

        //Assert
        assertNull(result);
        verifyNoInteractions(storageServiceMock);
    }

    @Test
    void get_ResourceNotFound() {
        final String fileName = "INVALIDFILENAME.txt";
        URI fileUri = Path.of("src/test/java/com/test_apps/motorola_coding_challenge/repository/INVALIDFILENAME.txt").toUri();
        when(storageServiceMock.buildFilePathUri(fileName)).thenReturn(fileUri);

        // Act
        Resource result = fileRepository.get(fileName);

        //Assert
        assertNull(result);
        verify(storageServiceMock, times(1)).buildFilePathUri(fileName);
    }

    @Test
    void save_Succeeds() throws IOException {
        // Arrange
        final String fileName = "validName.txt";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);
        final InputStream inputStreamMock = mock(InputStream.class);

        when(fileMock.getName()).thenReturn(fileName);
        when(fileMock.getInputStream()).thenReturn(inputStreamMock);
        when(storageServiceMock.buildFilePath(fileName)).thenReturn(path);

        // Act
        String result = fileRepository.save(fileMock);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result);
    }

    @Test
    void save_NoFileName() {
        // Arrange
        final MultipartFile fileMock = mock(MultipartFile.class);

        when(fileMock.getName()).thenReturn("");

        // Act
        final var result = fileRepository.save(fileMock);

        // Assert
        assertNull(result);
        verifyNoInteractions(storageServiceMock);
    }

    @Test
    void save_NoFileContent() throws IOException {
        // Arrange
        final String fileName = "validName.txt";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);

        when(fileMock.getName()).thenReturn(fileName);
        when(fileMock.getInputStream()).thenReturn(null);
        when(storageServiceMock.buildFilePath(fileName)).thenReturn(path);

        // Act
        String result = fileRepository.save(fileMock);

        // Assert
        assertNull(result);
    }
}
