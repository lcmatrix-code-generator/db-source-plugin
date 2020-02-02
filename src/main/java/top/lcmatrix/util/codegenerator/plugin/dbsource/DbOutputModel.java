package top.lcmatrix.util.codegenerator.plugin.dbsource;

import lombok.Data;
import top.lcmatrix.util.codegenerator.common.plugin.IOutputModel;

@Data
public class DbOutputModel implements IOutputModel {
    private Entity entity;
}
