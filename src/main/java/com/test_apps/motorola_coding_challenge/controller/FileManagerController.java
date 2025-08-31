package com.test_apps.motorola_coding_challenge.controller;

import com.test_apps.motorola_coding_challenge.service.FileService;
import com.test_apps.motorola_coding_challenge.model.FileListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/fileManager")
@RequiredArgsConstructor
public class FileManagerController {

    private final FileService fileService;

    // GET /fileManager
    @GetMapping
    public ResponseEntity<FileListDto> listFiles() {
        final List<String> files = fileService.listFiles();
        final FileListDto dto = new FileListDto(files);
        return ResponseEntity.ok(dto);
    }

    // GET /fileManager/{*filepath}
    @GetMapping("/{*filepath}") // search file inside a specific subdirectory
    public ResponseEntity<Resource> getFile(@PathVariable String filepath) {
        Resource resource = fileService.getFile(filepath);
        return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" + resource.getFilename() + "\"")
                 .body(resource);
    }

    // POST /fileManager
    @PostMapping("/{*filepath}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = fileService.postFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFileName);
    }

    // DELETE /fileManager/{*filepath}
    @DeleteMapping("/{*filepath}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filepath) {
        fileService.deleteFile(filepath);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
