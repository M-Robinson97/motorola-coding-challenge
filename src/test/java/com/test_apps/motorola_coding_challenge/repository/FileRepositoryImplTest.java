package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.model.FileListDto;
import com.test_apps.motorola_coding_challenge.repository.exception.FileNotFoundException;
import com.test_apps.motorola_coding_challenge.repository.exception.FileSystemException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.test_apps.motorola_coding_challenge.repository.exception.ExceptionMessages.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileRepositoryImplTest {
    @InjectMocks
    FileRepositoryImpl fileRepository;
    @Mock
    StorageService storageServiceMock;

    @Test
    void getAllFileNames_SucceedsWithFiles() throws Exception {
        // Arrange
        final String root = "\\root\\";
        final String fileOne = root + "fileOne.txt";
        final String fileTwo = root + "dirOne\\fileTwo.txt";
        final String fileThree = root + "dirTwo\\dirThree\\fileThree.txt";

        final Path rootPath = Path.of(root);

        final List<String> paths = Arrays.asList(fileOne, fileTwo, fileThree);

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenReturn(paths);

        final FileListDto expected = new FileListDto(paths);

        // Act
        final FileListDto result = fileRepository.getAllFileNames();

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void getAllFileNames_SucceedsWithNoFiles() throws Exception {
        // Arrange
        final String root = "\\root\\";
        final Path rootPath = Path.of(root);

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenReturn(Collections.emptyList());

        // Act
        final FileListDto result = fileRepository.getAllFileNames();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getFileNames());
        assertEquals(0, result.getFileNames().size());
    }

    @Test
    void getAllFileNames_ErrorFromService() throws Exception {
        // Arrange
        final String root = "\\root\\";
        final Path rootPath = Path.of(root);

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenThrow(new Exception());

        // Act
        final Exception exception = assertThrows(Exception.class, fileRepository::getAllFileNames);

        // Assert
        assertEquals(GET_ALL_FILES_FAILS, exception.getMessage());
    }

    @Test
    void get_Succeeds() throws Exception {
        // Arrange
        final String fileName = "FileRepositoryImplTest.java";
        final Path filePath = Path.of("src\\test\\java\\com\\test_apps\\motorola_coding_challenge\\repository\\FileRepositoryImplTest.java");
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
    void get_SucceedsWithExtraSlash() throws Exception {
        // Arrange
        final String fileName = "\\FileRepositoryImplTest.java";
        final Path filePath = Path.of("src\\test\\java\\com\\test_apps\\motorola_coding_challenge\\repository\\\\FileRepositoryImplTest.java");
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
    void get_ServiceError() throws Exception {
        // Arrange
        final String fileNameMock = "FileRepositoryImplTest.java";
        final Path filePathMock = mock(Path.class);
        final URI fileUriMock = mock(URI.class);
        final Resource resourceMock = mock(UrlResource.class);

        when(storageServiceMock.createFilePath(fileNameMock)).thenReturn(filePathMock);
        when(storageServiceMock.createUri(filePathMock)).thenReturn(fileUriMock);
        when(storageServiceMock.createResource(fileUriMock)).thenThrow(new FileSystemException(GET_FILE_FAILS));

        // Act
        final Exception exception = assertThrows(FileSystemException.class, () -> fileRepository.get(fileNameMock));

        //Assert
        assertEquals(GET_FILE_FAILS, exception.getMessage());
        verifyNoMoreInteractions(resourceMock);
    }

    @Test
    void get_ResourceNotFound() throws Exception {
        // Arrange
        final String fileNameMock = "FileRepositoryImplTest.java";
        final Path filePathMock = mock(Path.class);
        final URI fileUriMock = mock(URI.class);
        final Resource resourceMock = mock(UrlResource.class);

        when(storageServiceMock.createFilePath(fileNameMock)).thenReturn(filePathMock);
        when(storageServiceMock.createUri(filePathMock)).thenReturn(fileUriMock);
        when(storageServiceMock.createResource(fileUriMock)).thenReturn(resourceMock);
        when(resourceMock.exists()).thenReturn(false);

        // Act
        final Exception exception = assertThrows(FileNotFoundException.class, () -> fileRepository.get(fileNameMock));

        //Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
        verify(resourceMock, times(1)).exists();
        verifyNoMoreInteractions(resourceMock);
    }

    @Test
    void get_ResourceNotReadable() throws Exception {
        //Arrange
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
        final Exception exception = assertThrows(FileNotFoundException.class, () -> fileRepository.get(fileNameMock));

        //Assert
        assertEquals(FILE_NOT_FOUND_ERROR, exception.getMessage());
        verify(resourceMock, times(1)).exists();
        verify(resourceMock, times(1)).isReadable();
    }

    @Test
    void save_Succeeds() throws Exception {
        // Arrange
        final String fileName = "validName.txt";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);
        final InputStream inputStreamMock = mock(InputStream.class);

        when(fileMock.getInputStream()).thenReturn(inputStreamMock);
        when(storageServiceMock.createFilePath(fileName)).thenReturn(path);

        // Act
        final String result = fileRepository.save(fileMock, fileName);

        // Assert
        assertNotNull(result);
        assertEquals(fileName, result);
    }

    @Test
    void save_InputStreamThrows() throws Exception {
        // Arrange
        final String fileName = "validName.txt";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(path);
        when(fileMock.getInputStream()).thenThrow(new IOException());

        // Act
        final FileSystemException exception = assertThrows(FileSystemException.class, () -> fileRepository.save(fileMock, fileName));

        // Assert
        assertEquals(SAVE_FILE_FAILS, exception.getMessage());
    }

    @Test
    void delete_Succeeds() throws Exception {
        // Arrange
        final String fileName = "deleteMe.txt";
        final Path pathMock = mock(Path.class);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(pathMock);

        // Act
        fileRepository.delete(fileName);

        // Assert
        verify(storageServiceMock, times(1)).deleteFile(pathMock);
    }

    @Test
    void delete_CannotFindFile() throws Exception {
        // Arrange
        final String fileName = "deleteMe.txt";
        final Path pathMock = mock(Path.class);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(pathMock);
        doThrow(new FileNotFoundException(DELETE_FILE_FAILS)).when(storageServiceMock).deleteFile(pathMock);

        // Act
        Exception exception = assertThrows(Exception.class, () -> fileRepository.delete(fileName));

        // Assert
        assertEquals(DELETE_FILE_FAILS, exception.getMessage());
        verify(storageServiceMock, times(1)).deleteFile(pathMock);
    }
}
