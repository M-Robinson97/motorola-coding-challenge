package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.exception.FileNotFoundException;
import com.test_apps.motorola_coding_challenge.exception.FileSystemException;
import com.test_apps.motorola_coding_challenge.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.FILE_NOT_FOUND_ERROR;
import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.FILE_SYSTEM_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void listFiles_EmptyList() throws Exception {
        // Arrange
        when(fileRepositoryMock.getAllFileNames()).thenReturn(Collections.emptyList());

        // Act
        final List<String> result = fileService.listFiles();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void listFiles_ErrorThrown() throws Exception {
        // Arrange
        when(fileRepositoryMock.getAllFileNames()).thenThrow(new FileSystemException(FILE_SYSTEM_ERROR));

        // Act
        final Exception exception = assertThrows(FileSystemException.class, () -> fileService.listFiles());

        // Assert
        assertEquals(FILE_SYSTEM_ERROR, exception.getMessage());
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
        when(fileRepositoryMock.get(fileName)).thenThrow(new FileNotFoundException(FILE_NOT_FOUND_ERROR));

        // Act
        final Exception exception = assertThrows(FileNotFoundException.class, () -> fileService.getFile(fileName));

        // Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
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
        when(fileRepositoryMock.save(fileMock)).thenThrow(new FileSystemException(FILE_SYSTEM_ERROR));

        // Act
        final Exception result = assertThrows(FileSystemException.class, () -> fileService.postFile(fileMock));

        // Assert
        assertEquals(FILE_SYSTEM_ERROR, result.getMessage());
    }

    @Test
    void deleteFile_FileDeleted() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";

        // Act
        fileService.deleteFile(fileName);

        // Assert
        verify(fileRepositoryMock, times(1)).delete(fileName);
    }

    @Test
    void deleteFile_FileNotDeleted() throws Exception {
        // Arrange
        final String fileName = "fileName.txt";
        doThrow(new FileNotFoundException(FILE_NOT_FOUND_ERROR)).when(fileRepositoryMock).delete(fileName);

        // Act
        final Exception exception = assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(fileName));

        // Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
    }
}
