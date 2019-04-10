package com.cm.deity;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author chenming
 * @version 1.0
 * @Description 描述
 * @date 2019/4/10
 */
@SpringBootApplication
public class DeityApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DeityApplication.class);
    }
}
