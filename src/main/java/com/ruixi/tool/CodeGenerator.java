package com.ruixi.tool;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.*;

public class CodeGenerator {

    public static void main(String[] args) {
        //用来获取Mybatis-Plus.properties文件的配置信息
        final ResourceBundle rb = ResourceBundle.getBundle("mybatis-plus");

        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor(rb.getString("author"));
        gc.setOutputDir(rb.getString("OutputDir"));
        gc.setFileOverride(true);// 是否覆盖同名文件，默认是false
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setSwagger2(true);
        gc.setDateType(DateType.ONLY_DATE);

        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        gc.setEntityName("%sEntity");
        gc.setControllerName("%sController");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setMapperName("%sMapper");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername(rb.getString("userName"));
        dsc.setPassword(rb.getString("password"));
        dsc.setUrl(rb.getString("url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setRestControllerStyle(true);
        strategy.setEntityLombokModel(true);
        strategy.setSuperControllerClass("com.rs.common.web.BaseController");
        strategy.setSuperEntityClass("com.rs.common.baseentity.BaseEntity");
        strategy.setTablePrefix(rb.getString("tablePrefix").split(","));// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(rb.getString("tableName").split(","));
        strategy.setSuperEntityColumns(new String[]{"id", "create_time", "update_time", "del_flag", "create_user", "update_user", "sort"});
        strategy.setSuperServiceClass(null);
        strategy.setSuperServiceImplClass(null);
        strategy.setSuperMapperClass(null);

        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        String childFolder = rb.getString("className");

        pc.setParent(rb.getString("parent"));
        pc.setController("controller." + childFolder);
        pc.setService("service." + childFolder);
        pc.setServiceImpl("service." + childFolder + ".impl");
        pc.setMapper("mapper." + childFolder);
        pc.setEntity("module." + childFolder);
        pc.setXml("mapper." + childFolder);
        mpg.setPackageInfo(pc);

        //模板
        InjectionConfig config = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                this.setMap(map);
            }
        };

        List<FileOutConfig> files = new ArrayList<FileOutConfig>();
        files.add(new FileOutConfig("/template/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + pc.getController()).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getControllerName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/service.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + pc.getService()).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getServiceName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/serviceImpl.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + pc.getServiceImpl()).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getServiceImplName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + pc.getEntity()).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getEntityName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/mapper.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + pc.getMapper()).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".java"), tableInfo.getMapperName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/const.vue.js.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + "vue.const" + childFolder).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".js"), tableInfo.getEntityName());
                return entityFile;
            }
        });
        files.add(new FileOutConfig("/template/api.vue.js.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String expand = gc.getOutputDir() + (File.separator + pc.getParent() + "." + "vue.api." + childFolder).replace(".", File.separator);
                String entityFile = String.format((expand + File.separator + "%s" + ".js"), tableInfo.getEntityName());
                return entityFile;
            }
        });
        config.setFileOutConfigList(files);
        mpg.setCfg(config);

        TemplateConfig tc = new TemplateConfig();
        tc.setController(null);
        tc.setEntity(null);
        tc.setMapper(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        //tc.setXml(null);
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
        System.out.println("生成完成！！！！！！！！！！！！！！！！！！！！！！！");
    }
}
