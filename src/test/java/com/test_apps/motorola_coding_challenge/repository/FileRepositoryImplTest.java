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

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.test_apps.motorola_coding_challenge.utility.ExceptionMessages.*;
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
        final Path fileOnePath = Path.of(fileOne);
        final Path fileTwoPath = Path.of(fileTwo);
        final Path fileThreePath = Path.of(fileThree);

        final Stream<Path> paths = Stream.of(fileOnePath, fileTwoPath, fileThreePath);

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenReturn(paths);

        final List<String> expected = Arrays.asList(fileOne, fileTwo, fileThree);

        // Act
        final List<String> result = fileRepository.getAllFileNames();

        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void getAllFileNames_SucceedsWithNoFiles() throws Exception {
        // Arrange
        final String root = "\\root\\";
        final Path rootPath = Path.of(root);

        final Stream<Path> paths = Stream.empty();

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenReturn(paths);

        // Act
        final List<String> result = fileRepository.getAllFileNames();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getAllFileNames_ErrorFromService() throws Exception {
        // Arrange
        final String root = "\\root\\";
        final Path rootPath = Path.of(root);

        when(storageServiceMock.getPathFromRoot()).thenReturn(rootPath);
        when(storageServiceMock.getAllPaths(rootPath)).thenThrow(new Exception());

        // Act
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.getAllFileNames();
        });

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
    void get_NoFileName() throws Exception {
        // Arrange
        final String fileName = "";

        // Act
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.get(fileName);
        });

        //Assert
        assertEquals(GET_FILE_FAILS, exception.getMessage());
        assertEquals(FILE_NAME_REQUIRED, exception.getCause().getMessage());
        verifyNoInteractions(storageServiceMock);
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
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.get(fileNameMock);
        });

        //Assert
        assertEquals(GET_FILE_FAILS, exception.getMessage());
        assertEquals(FILE_NOT_FOUND, exception.getCause().getMessage());
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
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.get(fileNameMock);
        });

        //Assert
        assertEquals(GET_FILE_FAILS, exception.getMessage());
        assertEquals(FILE_NOT_FOUND, exception.getCause().getMessage());
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
    void save_NoFileName() throws Exception {
        // Arrange
        final MultipartFile fileMock = mock(MultipartFile.class);

        when(fileMock.getName()).thenReturn("");

        // Act
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.save(fileMock);
        });

        // Assert
        assertEquals(SAVE_FILE_FAILS, exception.getMessage());
        assertEquals(FILE_NAME_REQUIRED, exception.getCause().getMessage());
        verifyNoInteractions(storageServiceMock);
    }

    @Test
    void save_InputStreamThrows() throws Exception {
        // Arrange
        final String fileName = "validName.txt";
        final Path path = Path.of(fileName);
        final MultipartFile fileMock = mock(MultipartFile.class);

        when(fileMock.getName()).thenReturn(fileName);
        when(fileMock.getInputStream()).thenThrow(new RuntimeException(SAVE_FILE_FAILS));
        when(storageServiceMock.createFilePath(fileName)).thenReturn(path);

        // Act
        final Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.save(fileMock);
        });

        // Assert
        assertEquals(SAVE_FILE_FAILS, exception.getMessage());
    }

    @Test
    void delete_Succeeds() {
        // Arrange
        final String fileName = "deleteMe.txt";
        final Path pathMock = mock(Path.class);

        when(storageServiceMock.createFilePath(fileName)).thenReturn(pathMock);

        // Act
        final boolean result = fileRepository.delete(fileName);

        // Assert
        assertTrue(result);
    }

    @Test
    void delete_NoFileName() {
        // Arrange
        final String fileName = "";

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.delete(fileName);
        });

        // Assert
        assertEquals(DELETE_FILE_FAILS, exception.getMessage());
        assertEquals(FILE_NAME_REQUIRED, exception.getCause().getMessage());
        verifyNoInteractions(storageServiceMock);
    }

    @Test
    void delete_CannotFindFile() throws Exception {
        // Arrange
        final String fileName = "deleteMe.txt";
        final Path pathMock = mock(Path.class);
        final String expectedMessage = "";

        when(storageServiceMock.createFilePath(fileName)).thenReturn(pathMock);
        doThrow(new Exception()).when(storageServiceMock).deleteFile(pathMock);

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            fileRepository.delete(fileName);
        });

        // Assert
        assertEquals(DELETE_FILE_FAILS, exception.getMessage());
        verify(storageServiceMock, times(1)).deleteFile(pathMock);
    }
}
