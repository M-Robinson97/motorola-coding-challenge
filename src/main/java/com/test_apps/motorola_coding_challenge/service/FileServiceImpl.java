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
    public List<String> listFiles() throws Exception {
        return fileRepository.getAllFileNames();
    }

    @Override
    public Resource getFile(String fileName) throws Exception {
        return fileRepository.get(fileName);
    }

    @Override
    public String postFile(MultipartFile file) throws Exception {
        return fileRepository.save(file);
    }

    @Override
    public void deleteFile(String fileName) throws Exception {
        fileRepository.delete(fileName);
    }
}
