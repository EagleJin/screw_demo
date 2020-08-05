package org.sloth.databasedocgenerator;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DatabaseDocGeneratorApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // 生成配置文件
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径，根据自己情况修改
                .fileOutputDir("D;/docs")
                // 打开目录
                .openOutputDir(true)
                // 文件类型(HTML、Markdown、Doc)
                .fileType(EngineFileType.WORD)
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker)
                // 生成的文件名称
                .customTemplate("cps数据库设计文档")
                .build();
        // 生成文档配置（包含以下自定义版本号、描述等配置连接）
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .description("CPS数据库设计说明")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();
        // 执行生成
        new DocumentationExecute(config).execute();
    }

    public static ProcessConfig getProcessConfig() {
        // 忽略表名
        List<String> ignoreTableName = Arrays.asList("test_user", "test_group");
        // 忽略表前缀，如忽略a、t开头的数据库表
        List<String> ignorePrefix = Arrays.asList("test_");
        // 忽略表后缀
        List<String> ignoreSuffix = Arrays.asList("_test");
        return ProcessConfig.builder()
                // 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                // 根据指定表名称生成
//                .designatedTableName(Arrays.asList("cps_cp_content"))
                .designatedTableName(new ArrayList<>())
                // 根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                // 根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                // 忽略表名
                .ignoreTableName(ignoreTableName)
                // 忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                // 忽略表后缀
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }

}
