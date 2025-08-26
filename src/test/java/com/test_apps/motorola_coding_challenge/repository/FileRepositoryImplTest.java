package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.service.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
    void get_Succeeds() throws Exception {
        final String fileName = "FileRepositoryImplTest.java";
        final Path filePath = Path.of("src/test/java/com/test_apps/motorola_coding_challenge/repository/FileRepositoryImplTest.java");
        final URI fileUri = filePath.toUri();
        final Resource resource = new UrlResource(fileUri);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(filePath);
        when(storageServiceMock.createUri(filePath)).thenReturn(fileUri);
        when(storageServiceMock.createResource(fileUri)).thenReturn(resource);

        // Act
        Resource result = fileRepository.get(fileName);

        //Assert
        assertNotNull(result);
        assertEquals("FileRepositoryImplTest.java", Path.of(result.getURI()).getFileName().toString());
    }

    @Test
    void get_SucceedsWithExtraSlash() throws Exception {
        final String fileName = "/FileRepositoryImplTest.java";
        final Path filePath = Path.of("src/test/java/com/test_apps/motorola_coding_challenge/repository//FileRepositoryImplTest.java");
        final URI fileUri = filePath.toUri();
        final Resource resource = new UrlResource(fileUri);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(filePath);
        when(storageServiceMock.createUri(filePath)).thenReturn(fileUri);
        when(storageServiceMock.createResource(fileUri)).thenReturn(resource);

        // Act
        final Resource result = fileRepository.get(fileName);

        //Assert
        assertNotNull(result);
        assertEquals("FileRepositoryImplTest.java", Path.of(result.getURI()).getFileName().toString());
    }

    @Test
    void get_NoFileName() {
        final String fileName = "";

        // Act
        final Resource result = fileRepository.get(fileName);

        //Assert
        assertNull(result);
        verifyNoInteractions(storageServiceMock);
    }

    @Test
    void get_ResourceNotFound() throws Exception {
        final String fileNameMock = "FileRepositoryImplTest.java";;
        final Path filePathMock = mock(Path.class);
        final URI fileUriMock = mock(URI.class);
        final Resource resourceMock = mock(UrlResource.class);

        when(storageServiceMock.createFilePath(fileNameMock)).thenReturn(filePathMock);
        when(storageServiceMock.createUri(filePathMock)).thenReturn(fileUriMock);
        when(storageServiceMock.createResource(fileUriMock)).thenReturn(resourceMock);
        when(resourceMock.exists()).thenReturn(false);

        // Act
        final Resource result = fileRepository.get(fileNameMock);

        //Assert
        assertNull(result);
        verify(resourceMock, times(1)).exists();
        verifyNoMoreInteractions(resourceMock);
    }

    @Test
    void get_ResourceNotReadable() throws Exception {
        final String fileNameMock = "FileRepositoryImplTest.java";
        final Path filePathMock = mock(Path.class);
        final URI fileUriMock = mock(URI.class);
        final Resource resourceMock = mock(UrlResource.class);

        when(storageServiceMock.createFilePath(fileNameMock)).thenReturn(filePathMock);
        when(storageServiceMock.createUri(filePathMock)).thenReturn(fileUriMock);
        when(storageServiceMock.createResource(fileUriMock)).thenReturn(resourceMock);
        when(resourceMock.exists()).thenReturn(true);
        when(resourceMock.isReadable()).thenReturn(false);

        // Act
        final Resource result = fileRepository.get(fileNameMock);

        //Assert
        assertNull(result);
        verify(resourceMock, times(1)).exists();
        verify(resourceMock, times(1)).isReadable();
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
        when(storageServiceMock.createFilePath(fileName)).thenReturn(path);

        // Act
        final String result = fileRepository.save(fileMock);

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
        when(storageServiceMock.createFilePath(fileName)).thenReturn(path);

        // Act
        final String result = fileRepository.save(fileMock);

        // Assert
        assertNull(result);
    }
}
