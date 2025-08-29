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

    // GET /fileManager/{filename}
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = fileService.getFile(filename);
         return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, filename)
                 .body(file);
    }

    // POST /fileManager
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = fileService.postFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFileName);
    }

    // DELETE /fileManager/{filename}
    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@PathVariable String filename) {
        fileService.deleteFile(filename);
        return ResponseEntity.noContent().build();
    }
}
