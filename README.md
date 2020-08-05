### 说明
#### 修改数据源
> src/test/resource 下 application.properties修改数据库连接

#### 修改生成文档设置
> 文件：src/test/java 下 DatabaseDocGeneratorApplicationTests.java  

> 第31行，根据实际修改路径

> 第35行，根据实际需要修改生成文档格式

> 第39行，设置生成文件的名称

> 第43行，设置文档的版本号

> 第53行，getProcessConfig 方法中设置生成指定表、忽略表等

### 运行
#### DatabaseDocGeneratorApplicationTests 文件中 右键 运行 contextLoads() 即可。