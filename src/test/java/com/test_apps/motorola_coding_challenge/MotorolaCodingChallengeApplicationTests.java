package com.test_apps.motorola_coding_challenge;

import com.test_apps.motorola_coding_challenge.controller.FileManagerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MotorolaCodingChallengeApplicationTests {
    @Autowired
    private FileManagerController controller;

	@Test
	void contextLoads() {
        assertThat(controller).isNotNull();
	}

}
