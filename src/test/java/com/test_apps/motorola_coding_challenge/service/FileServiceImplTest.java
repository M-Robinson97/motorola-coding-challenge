package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
Minimal tests as service is pass-through for MVP
 */
@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {
    @Mock
    FileRepository fileRepositoryMock;
    @InjectMocks
    FileServiceImpl fileService;

    @Test
    void listFiles_FilesFound() throws Exception {
        // Arrange
        final List<String> expected = Arrays.asList("name1", "name2", "name3");
        when(fileRepositoryMock.getAllFileNames()).thenReturn(expected);

        // Act
        final List<String> result = fileService.listFiles();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void listFiles_NoFiles() throws Exception {
        // Arrange
        when(fileRepositoryMock.getAllFileNames()).thenReturn(null);

        // Act
        final List<String> result = fileService.listFiles();

        // Assert
        assertNull(result);
    }

    @Test
    void getFile_FileFound() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";
        final Resource resourceMock = mock(Resource.class);
        when(fileRepositoryMock.get(fileName)).thenReturn(resourceMock);

        // Act
        final Resource result = fileService.getFile(fileName);

        // Assert
        assertEquals(resourceMock, result);
    }

    @Test
    void getFile_NoFiles() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";
        when(fileRepositoryMock.get(fileName)).thenReturn(null);

        // Act
        final Resource result = fileService.getFile(fileName);

        // Assert
        assertNull(result);
    }

    @Test
    void postFile_FileCreated() throws Exception {
        // Arrange
        final MultipartFile fileMock = mock(MultipartFile.class);
        final String fileName = "fileName.txt";
        when(fileRepositoryMock.save(fileMock)).thenReturn(fileName);

        // Act
        final String result = fileService.postFile(fileMock);

        // Assert
        assertEquals(fileName, result);
    }

    @Test
    void postFile_FileNotCreated() throws Exception {
        // Arrange
        final MultipartFile fileMock = mock(MultipartFile.class);
        when(fileRepositoryMock.save(fileMock)).thenReturn(null);

        // Act
        final String result = fileService.postFile(fileMock);

        // Assert
        assertNull(result);
    }

    @Test
    void deleteFile_FileDeleted() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";
        when(fileRepositoryMock.delete(fileName)).thenReturn(true);

        // Act
        final boolean result = fileService.deleteFile(fileName);

        // Assert
        assertTrue(result);
    }

    @Test
    void deleteFile_FileNotDeleted() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";
        when(fileRepositoryMock.delete(fileName)).thenReturn(false);

        // Act
        final boolean result = fileService.deleteFile(fileName);

        // Assert
        assertFalse(result);
    }
}
