package top.lcmatrix.util.codegenerator.plugin.dbsource;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.JdbcDatabasePlatformFactory;
import top.lcmatrix.util.codegenerator.plugin.dbsource.util.AsteriskExp;
import top.lcmatrix.util.codegenerator.plugin.dbsource.util.JarLoader;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class DbStructDao {

	private IDatabasePlatform platform;
	private List<String> allTableNames;
	private HikariDataSource ds;
	
	public DbStructDao(DbInputModel inputModel) {
		if(inputModel.getJdbcDriverJar() != null
				&& StringUtils.isNotBlank(inputModel.getJdbcDriverJar().getName())
				&& !"Mysql".equalsIgnoreCase(inputModel.getJdbcDriverJar().getName())) {
			JarLoader.loadLocalJar(inputModel.getJdbcDriverJar().getAbsolutePath());
            reloadDrivers();
        }else{
			loadMysqlDriver();
		}
		try {
			ds = new HikariDataSource();
			ds.setJdbcUrl(inputModel.getJdbcUrl());
			ds.setUsername(inputModel.getUserName());
			ds.setPassword(inputModel.getPassword());
			ds.setConnectionTimeout(5000);
			ds.setMaximumPoolSize(5);
//		ds.setIdleTimeout(30000);
			platform = JdbcDatabasePlatformFactory.createNewPlatformInstance(ds, null, false, true);
			if(platform == null) {
				throw new RuntimeException("Unsuportted database!");
			}
			allTableNames = platform.readTableNamesFromDatabase(null, null, null);
		} catch (Exception e) {
			if(ds != null){
				ds.close();
			}
			throw e;
		}
	}

	private void loadMysqlDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

    private void reloadDrivers() {
        ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        Iterator<Driver> driversIterator = loadedDrivers.iterator();
        while (driversIterator.hasNext()){
            driversIterator.next();
        }
    }

	public List<Table> getTableModels(String tableNameExp){
		List<Table> tables = new ArrayList<>();
		AsteriskExp asteriskExp = new AsteriskExp(tableNameExp, true);
		for(String tName : allTableNames) {
			if(asteriskExp.match(tName)) {
				Table t = platform.getDdlReader().readTable(null, null, tName);
				if(t != null) {
					tables.add(t);
				}
			}
		}
		ds.close();
		return tables;
	}
}
