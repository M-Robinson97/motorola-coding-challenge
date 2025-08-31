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

/**
 * REST controller for managing files in the application.
 * Provides endpoints to list, download, upload and delete files
 * under the configured storage root.
 * Base URL: /fileManager
 */
@RestController
@RequestMapping("/fileManager")
@RequiredArgsConstructor
public class FileManagerController {

    private final FileService fileService;

    /**
     * GET /fileManager
     * Returns a list of names of currently stored files including their subdirectories.
     * @return 200 OK with a {@link FileListDto} containing file names
     */
    @GetMapping
    public ResponseEntity<FileListDto> listFiles() {
        final FileListDto dto = fileService.listFiles();
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /fileManager/{*filepath}
     * Retrieves the file located at the given relative path.
     * @param filepath the relative path to the file (subdirectories supported)
     * @return 200 OK with the file resource,
     *         404 Not Found if the file does not exist
     */
    @GetMapping("/{*filepath}") // search file inside a specific subdirectory
    public ResponseEntity<Resource> getFile(@PathVariable String filepath) {
        Resource resource = fileService.getFile(filepath);
        return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION,  "attachment; filename=\"" + resource.getFilename() + "\"")
                 .body(resource);
    }

    /**
     * POST /fileManager/{*filepath}
     * Uploads a new file to the given path (including subdirectories).
     * @param file the multipart file to upload
     * @return 201 Created with the stored file name,
     *         400 Bad Request if input is invalid
     */
    @PostMapping("/{*filepath}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String savedFileName = fileService.postFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFileName);
    }

    /**
     * DELETE /fileManager/{*filePath}
     * Deletes the specified file from storage.
     * @param filepath the relative path of the file to delete
     * @return 200 OK if deletion succeeds,
     *         404 Not Found if the file does not exist
     */
    @DeleteMapping("/{*filepath}")
    public ResponseEntity<Void> deleteFile(@PathVariable String filepath) {
        fileService.deleteFile(filepath);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
