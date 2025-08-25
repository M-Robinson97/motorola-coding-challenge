package com.test_apps.motorola_coding_challenge.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Repository
public class FileRepositoryImpl implements FileRepository {
    @Override
    public List<String> getAllNames() {
        log.info("Getting all files by name");
        return List.of();
    }

    @Override
    public Resource get(String fileName) {
        log.info("Getting file with name: {}", fileName);
        return null;
    }

    @Override
    public String save(MultipartFile file) {
        log.info("Saving file with name: {}", file.getName());
        return "";
    }

    @Override
    public void delete(String fileName) {
        log.info("Deleting file with name: {}", fileName);
    }
}
