package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JUnitPlatform.class)
@SelectPackages("com.example.demo.controllers")
public class SareetaApplicationTests {

	/*@Test
	public void contextLoads() {
	}*/

	// reference code: https://howtodoinjava.com/junit5/junit5-test-suites-examples/
}
