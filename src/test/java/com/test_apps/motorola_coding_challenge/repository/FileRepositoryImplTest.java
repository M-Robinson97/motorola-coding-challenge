package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileRepositoryImplTest {
    @InjectMocks
    FileRepositoryImpl fileRepository;
    @Mock
    StorageService storageServiceMock;

    @Test
    void save_Succeeds() throws IOException {
        // Arrange
        final String fileName = "validName";
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
    }

    @Test
    void save_NoFileContent() throws IOException {
        // Arrange
        final String fileName = "validName";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);
        final InputStream inputStreamMock = mock(InputStream.class);

        when(fileMock.getName()).thenReturn(fileName);
        when(fileMock.getInputStream()).thenReturn(null);
        when(storageServiceMock.buildFilePath(fileName)).thenReturn(path);

        // Act
        String result = fileRepository.save(fileMock);

        // Assert
        assertNull(result);
    }
}
