package com.test_apps.motorola_coding_challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileListDto {
    private List<String> fileNames;
}
