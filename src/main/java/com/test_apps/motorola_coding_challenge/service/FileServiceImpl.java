package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.exception.FileNameRequiredException;
import com.test_apps.motorola_coding_challenge.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.test_apps.motorola_coding_challenge.exception.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    public List<String> listFiles() {
        return fileRepository.getAllFileNames();
    }

    @Override
    public Resource getFile(String fileName) {
        String cleanFileName = this.cleanAndCheckFileName(fileName);
        return fileRepository.get(cleanFileName);
    }

    @Override
    public String postFile(MultipartFile file) {
        String cleanFileName = this.cleanAndCheckFileName(file.getOriginalFilename());
        return fileRepository.save(file, cleanFileName);
    }

    @Override
    public void deleteFile(String fileName) {
        String cleanFileName = this.cleanAndCheckFileName(fileName);
        fileRepository.delete(cleanFileName);
    }

    private String cleanAndCheckFileName (String fileName) {
        String cleanFileName = StringUtils.cleanPath(
                Optional.ofNullable(fileName)
                        .filter(name -> !name.isEmpty())
                        .orElseThrow(() -> new FileNameRequiredException(FILE_NAME_REQUIRED_ERROR))
        ).trim();
        if (cleanFileName.contains("..") || Paths.get(cleanFileName).isAbsolute()) {
            throw new SecurityException(INVALID_FILE_NAME_ERROR);
        }
        return cleanFileName;
    }
}
