package top.lcmatrix.util.codegenerator.plugin.dbsource;

import org.jumpmind.db.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.lcmatrix.util.codegenerator.common.plugin.AbstractSourcePlugin;
import top.lcmatrix.util.codegenerator.common.plugin.Global;

import java.util.ArrayList;
import java.util.List;

public class DbSourcePlugin extends AbstractSourcePlugin<DbInputModel, DbOutputModel> {

	@Override
	public List<DbOutputModel> getOutputModels(DbInputModel dbInputModel, Global global) {
		DbStructDao tableMetaDataDao = new DbStructDao(dbInputModel);
		List<Table> tableModels = tableMetaDataDao.getTableModels(dbInputModel.getTableName());
		if(tableModels.isEmpty()) {
			getLogger().info("Not found any table to process!");
			throw new RuntimeException("Not found any table to process!");
		}
		List<DbOutputModel> outputModels = new ArrayList<DbOutputModel>();
		for(Table table : tableModels) {
			outputModels.add(tableModel2DbOutputModel(table, dbInputModel));
		}
		return outputModels;
	}

	private DbOutputModel tableModel2DbOutputModel(Table table, DbInputModel inputModel) {
		DbOutputModel outputModel = new DbOutputModel();
		outputModel.setEntity(new Entity(table));
		return outputModel;
	}

}
