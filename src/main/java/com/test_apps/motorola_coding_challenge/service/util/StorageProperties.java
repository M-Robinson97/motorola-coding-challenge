package com.test_apps.motorola_coding_challenge.service.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Injectable component to make root storage path (i) accessible from a class in the application and (ii) modifiable
 * from integration tests.
 */
@Component
@ConfigurationProperties(prefix = "storage")
@Data
public class StorageProperties {
    private String root;

    public Path getRootPath() {
        return Paths.get(root).toAbsolutePath().normalize();
    }
}
