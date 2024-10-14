package com.vantara.hitachi.piics.runner;

import java.util.logging.Logger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class StartupRunner implements ApplicationRunner {
	public static final Logger logger = Logger.getLogger(StartupRunner.class.toString());

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}
}
