package org.sloth.databasedocgenerator;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 生成数据库表结构说明文档
 * 启动生成文档，文档生成后程序自动退出
 * @author sloth
 */
@SpringBootApplication
public class DatabaseDocGeneratorApplication {

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return () -> 42;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(
                SpringApplication.run(DatabaseDocGeneratorApplication.class, args)
        ));
    }

    @Bean
    public StartupRunner startupRunner() {
        return new StartupRunner();
    }
}
