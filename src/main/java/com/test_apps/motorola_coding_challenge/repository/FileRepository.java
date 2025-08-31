package com.test_apps.motorola_coding_challenge.repository;

import com.test_apps.motorola_coding_challenge.model.FileListDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {
    FileListDto getAllFileNames();

    Resource get(String fileName);

    String save(MultipartFile file, String fileName);

    void delete(String fileName);
}
