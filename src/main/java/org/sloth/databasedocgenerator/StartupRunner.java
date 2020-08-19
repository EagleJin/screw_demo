package org.sloth.databasedocgenerator;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Description:
 * @author: sloth
 * @Date: 2020/08/19/9:31
 */
@Slf4j
public class StartupRunner implements ApplicationRunner {
    @Value("${system.docs.output.dir:docs}")
    private String outputDir;
    @Value("${system.docs.file.type:Doc}")
    private String fileType;
    @Value("${system.docs.title}")
    private String docTitle;
    @Value("${system.docs.version}")
    private String docVersion;
    @Value("${system.docs.desc}")
    private String docDesc;
    @Value("#{'${system.docs.ignore.tableName}'.split(',')}")
    private List<String> ignoreTableName;
    @Value("#{'${system.docs.ignore.prefix}'.split(',')}")
    private List<String> ignorePrefix;
    @Value("#{'${system.docs.ignore.suffix}'.split(',')}")
    private List<String> ignoreSuffix;
    @Value("#{'${system.docs.designated.tableName}'.split(',')}")
    private List<String> designatedTableName;
    @Value("#{'${system.docs.designated.prefix}'.split(',')}")
    private List<String> designatedPrefix;
    @Value("#{'${system.docs.designated.suffix}'.split(',')}")
    private List<String> designatedSuffix;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Database design document start...");
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        // 生成配置文件
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径，根据自己情况修改
                .fileOutputDir(outputDir)
                // 打开目录
                .openOutputDir(true)
                // 文件类型(HTML、Markdown、Doc)
                .fileType(getEngineFileType(fileType))
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker)
                // 生成的文件名称
                .customTemplate(docTitle)
                .build();
        // 生成文档配置（包含以下自定义版本号、描述等配置连接）
        Configuration config = Configuration.builder()
                .version(docVersion)
                .description(docDesc)
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();
        // 执行生成
        new DocumentationExecute(config).execute();
        log.info("Database design document finish!!!");
    }

    /**
     * 选择文档类型
     * @param fileType
     * @return
     */
    private EngineFileType getEngineFileType(String fileType) {
        if (StringUtils.isBlank(fileType)) {
            return EngineFileType.WORD;
        } else if ("Doc".equalsIgnoreCase(fileType)) {
            return EngineFileType.WORD;
        } else if ("HTML".equalsIgnoreCase(fileType)) {
            return EngineFileType.HTML;
        } else if ("Markdown".equalsIgnoreCase(fileType))  {
            return EngineFileType.MD;
        }
        return EngineFileType.WORD;
    }

    public ProcessConfig getProcessConfig() {
        log.info("designatedTableName => {}", designatedTableName);
        log.info("designatedPrefix => {}", designatedPrefix);
        log.info("designatedSuffix => {}", designatedSuffix);
        log.info("ignoreTableName => {}", ignoreTableName);
        log.info("ignorePrefix => {}", ignorePrefix);
        log.info("ignoreSuffix => {}", ignoreSuffix);
        return ProcessConfig.builder()
                // 指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
                // 根据指定表名称生成
                .designatedTableName(designatedTableName)
                // 根据表前缀生成
                .designatedTablePrefix(designatedPrefix)
                // 根据表后缀生成
                .designatedTableSuffix(designatedSuffix)
                // 忽略表名
                .ignoreTableName(ignoreTableName)
                // 忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                // 忽略表后缀
                .ignoreTableSuffix(ignoreSuffix)
                .build();
    }
}
