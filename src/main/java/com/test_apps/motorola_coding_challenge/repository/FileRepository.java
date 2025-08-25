package com.test_apps.motorola_coding_challenge.repository;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileRepository {
    List<String> getAllNames();

    Resource get(String fileName);

    String save(MultipartFile file);

    void delete(String fileName);
}
