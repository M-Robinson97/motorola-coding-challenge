package com.test_apps.motorola_coding_challenge.service;

import com.test_apps.motorola_coding_challenge.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    @Override
    public List<String> listFiles() {
        return fileRepository.getAllNames();
    }

    @Override
    public Resource getFile(String fileName) {
        return fileRepository.get(fileName);
    }

    @Override
    public String postFile(MultipartFile file) {
        return fileRepository.save(file);
    }

    @Override
    public String deleteFile(String fileName) {
        return fileRepository.delete(fileName);
    }
}
