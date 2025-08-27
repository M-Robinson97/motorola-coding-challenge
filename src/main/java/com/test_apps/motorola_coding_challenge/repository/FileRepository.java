package com.test_apps.motorola_coding_challenge.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileRepository {
    List<String> getAllFileNames() throws Exception;

    Resource get(String fileName) throws Exception;

    String save(MultipartFile file) throws Exception;

    void delete(String fileName) throws Exception;
}
