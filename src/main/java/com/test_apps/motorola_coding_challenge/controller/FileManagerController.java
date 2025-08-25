package com.test_apps.motorola_coding_challenge.controller;

import com.test_apps.motorola_coding_challenge.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileManagerController {

    private final FileService fileService;
}
