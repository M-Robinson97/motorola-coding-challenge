package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.exception.FileNameRequiredException;
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

import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {
    @Mock
    FileRepository fileRepositoryMock;
    @InjectMocks
    FileServiceImpl fileService;

    @Test
    void listFiles_FilesFound() {
        // Arrange
        final List<String> expected = Arrays.asList("name1", "name2", "name3");
        when(fileRepositoryMock.getAllFileNames()).thenReturn(expected);

        // Act
        final List<String> result = fileService.listFiles();

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void listFiles_EmptyList() {
        // Arrange
        when(fileRepositoryMock.getAllFileNames()).thenReturn(Collections.emptyList());

        // Act
        final List<String> result = fileService.listFiles();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void listFiles_ErrorThrown() {
        // Arrange
        when(fileRepositoryMock.getAllFileNames()).thenThrow(new FileSystemException(FILE_SYSTEM_ERROR));

        // Act
        final Exception exception = assertThrows(FileSystemException.class, () -> fileService.listFiles());

        // Assert
        assertEquals(FILE_SYSTEM_ERROR, exception.getMessage());
    }

    @Test
    void getFile_FileFound() {
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
    void getFile_EmptyName() {
        // Arrange
        final String fileName = "";

        // Act
        final FileNameRequiredException exception = assertThrows(FileNameRequiredException.class, () -> fileService.getFile(fileName));

        // Assert
        assertEquals(FILE_NAME_REQUIRED_ERROR, exception.getMessage());
    }

    @Test
    void getFile_MaliciousName() {
        // Arrange
        final String fileName = "../";

        // Act
        final SecurityException exception = assertThrows(SecurityException.class, () -> fileService.getFile(fileName));

        // Assert
        assertEquals(INVALID_FILE_NAME_ERROR, exception.getMessage());
    }

    @Test
    void getFile_NotFound() {
        // Arrange
        final String fileName = "fileName.txt";
        when(fileRepositoryMock.get(fileName)).thenThrow(new FileNotFoundException(FILE_NOT_FOUND_ERROR));

        // Act
        final FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> fileService.getFile(fileName));

        // Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
    }

    @Test
    void postFile_FileCreated() {
        // Arrange
        final MultipartFile fileMock = mock(MultipartFile.class);
        final String fileName = "fileName.txt";
        when(fileMock.getOriginalFilename()).thenReturn(fileName);
        when(fileRepositoryMock.save(fileMock, fileName)).thenReturn(fileName);

        // Act
        final String result = fileService.postFile(fileMock);

        // Assert
        assertEquals(fileName, result);
    }

    @Test
    void postFile_FileNotCreated() {
        // Arrange
        final String fileName = "fileName.txt";
        final MultipartFile fileMock = mock(MultipartFile.class);
        when(fileMock.getOriginalFilename()).thenReturn(fileName);
        when(fileRepositoryMock.save(fileMock, fileName)).thenThrow(new FileSystemException(SAVE_FILE_FAILS));

        // Act
        final FileSystemException exception = assertThrows(FileSystemException.class, () -> fileService.postFile(fileMock));

        // Assert
        assertEquals(SAVE_FILE_FAILS, exception.getMessage());
    }

    @Test
    void postFile_EmptyName() {
        // Arrange
        final String fileName = "";
        final MultipartFile fileMock = mock(MultipartFile.class);
        when(fileMock.getOriginalFilename()).thenReturn(fileName);

        // Act
        final FileNameRequiredException result = assertThrows(FileNameRequiredException.class, () -> fileService.postFile(fileMock));

        // Assert
        assertEquals(FILE_NAME_REQUIRED_ERROR, result.getMessage());
    }

    @Test
    void postFile_MaliciousName() {
        // Arrange
        final String fileName = "../";
        final MultipartFile fileMock = mock(MultipartFile.class);
        when(fileMock.getOriginalFilename()).thenReturn(fileName);

        // Act
        final SecurityException exception = assertThrows(SecurityException.class, () -> fileService.postFile(fileMock));

        // Assert
        assertEquals(INVALID_FILE_NAME_ERROR, exception.getMessage());
    }

    @Test
    void deleteFile_FileDeleted() {
        // Arrange
        final String fileName = "fileName.txt";

        // Act
        fileService.deleteFile(fileName);

        // Assert
        verify(fileRepositoryMock, times(1)).delete(fileName);
    }

    @Test
    void deleteFile_EmptyName() {
        // Arrange
        final String fileName = "";

        // Act
        final FileNameRequiredException exception = assertThrows(FileNameRequiredException.class, () -> fileService.deleteFile(fileName));

        // Assert
        assertEquals(FILE_NAME_REQUIRED_ERROR, exception.getMessage());
    }

    @Test
    void deleteFile_MaliciousName() {
        // Arrange
        final String fileName = "../";

        // Act
        final SecurityException exception = assertThrows(SecurityException.class, () -> fileService.deleteFile(fileName));

        // Assert
        assertEquals(INVALID_FILE_NAME_ERROR, exception.getMessage());
    }

    @Test
    void deleteFile_FileNotDeleted() {
        // Arrange
        final String fileName = "fileName.txt";
        doThrow(new FileNotFoundException(FILE_NOT_FOUND_ERROR)).when(fileRepositoryMock).delete(fileName);

        // Act
        final FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(fileName));

        // Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
    }
}
