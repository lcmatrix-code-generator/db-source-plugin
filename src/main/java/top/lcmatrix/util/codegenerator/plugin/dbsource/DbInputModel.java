package top.lcmatrix.util.codegenerator.plugin.dbsource;

import lombok.Data;
import top.lcmatrix.util.codegenerator.common.plugin.InputField;

import java.io.File;

@Data
public class DbInputModel {

    @InputField(label = "jdbc driver jar ( Mysql is built-in supported,just leave it empty when using Mysql. )",
     fileSelectionMode = InputField.FILE_SELECTION_MODE_FILES_ONLY, allowFileSuffixes = ".jar", defaultValue = "Mysql")
    private File jdbcDriverJar;

    @InputField(label = "jdbc url", required = true)
    private String jdbcUrl;

    @InputField(label = "db user name", required = true)
    private String userName;

    @InputField(label = "db password", mask = true)
    private String password;

    @InputField(label = "table name ( * denotes any character )", required = true)
    private String tableName;
}
