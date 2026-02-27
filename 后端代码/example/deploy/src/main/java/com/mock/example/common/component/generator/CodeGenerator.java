package com.mock.example.common.component.generator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.*;

/**
 * mybatis 自动生成
 * 参考博客：https://blog.csdn.net/weixin_45526538/article/details/114548841
 *
 * 使用说明：
 *
 * 1.数据库相关的配置
 * 2.生成包所在路径配置
 * 3.生成模版配置
 *   模版可从mybatis-plus-generator源码包里面拷贝出来（可根据自己的需要去修改模版）
 *   然后放到工程resource/templates目录底下
 * 4.运行main方法，控制台提示后输入相应的表名生成
 *
 * 注意：生成的代码，如果文件不删掉是不会重新再生成了，
 *      所以添加修改字段时候，最好先备份下然后再重新生成
 *
 *
 * @author: Mock
 * @date: 2022-09-18 09:10:01
 */
public class CodeGenerator {

    /**
     * 1.数据库相关的配置
     */
    private static final String DATA_SOURCE_URL
            = "jdbc:mysql://localhost:3306/example-college-entrance?useUnicode=true&characterEncoding=UTF-8";
    private static final String DATA_SOURCE_USERNAME = "root";
    private static final String DATA_SOURCE_PASSWORD = "12345678";
    private static final String DATA_SOURCE_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * 2.生成包所在路径配置
     */
    private static final String PACKAGE_PARENT = "com.mock.example.common.component.generator.code";
    private static final String PACKAGE_ENTITY = "entity";
    private static final String PACKAGE_MAPPER = "mapper";
    private static final String PACKAGE_SERVICE = "service";
    private static final String PACKAGE_CONTROLLER = "controller";

    /**
     * 3.生成模版配置
     */
    private static final String TEMPLATES_ENTITY = "templates/entity.java";
    private static final String TEMPLATES_MAPPER = "templates/mapper.java";
    private static final String TEMPLATES_MAPPER_XML = "templates/mapper.xml";
    private static final String TEMPLATES_SERVICE = "templates/service.java";
    private static final String TEMPLATES_SERVICE_IMPL = "templates/serviceImpl.java";
    private static final String TEMPLATES_CONTROLLER = "templates/controller.java";

    /**
     * 读取控制台内容
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();

        help.append("请输入" + tip + "：");

        System.out.println(help.toString());

        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }

        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/deploy/src/main/java");

        //作者
        gc.setAuthor("Mybatis Auto");

        //打开输出目录
        gc.setOpen(false);
        //xml开启 BaseResultMap
        gc.setBaseResultMap(true);
        //xml 开启BaseColumnList
        gc.setBaseColumnList(true);

        //日期格式，采用Date
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DATA_SOURCE_URL);
        dsc.setDriverName(DATA_SOURCE_DRIVER);
        dsc.setUsername(DATA_SOURCE_USERNAME);
        dsc.setPassword(DATA_SOURCE_PASSWORD);
        mpg.setDataSource(dsc);


        //包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE_PARENT)
                .setEntity(PACKAGE_ENTITY)
                .setMapper(PACKAGE_MAPPER)
                .setService(PACKAGE_SERVICE)
                .setServiceImpl(PACKAGE_SERVICE + ".impl")
                .setController(PACKAGE_CONTROLLER);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> map = new HashMap<>();
                map.put("date1", "1.0.0");
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();

        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/deploy/src/main/resources/mapper/" +
                        tableInfo.getEntityName() + "Mapper"
                        + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig()
                .setEntity(TEMPLATES_ENTITY)
                .setMapper(TEMPLATES_MAPPER)
                .setXml(TEMPLATES_MAPPER_XML)
                .setService(TEMPLATES_SERVICE)
                .setServiceImpl(TEMPLATES_SERVICE_IMPL)
                .setController(TEMPLATES_CONTROLLER);

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        //lombok模型
        strategy.setEntityLombokModel(true);

        //生成 @RestController 控制器
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("请输入表名，多个的话英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);


        //表前缀
        //strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}


  