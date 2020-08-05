package org.sloth.databasedocgenerator.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * HikariCP连接池配置
 * @Description:
 * @author: sloth
 * @Date: 2020/08/05/11:53
 */
@Configuration
public class DataSourceConfiguration {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        //设置可以获取tables remarks信息
        config.addDataSourceProperty("useInformationSchema","true");
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }
}
