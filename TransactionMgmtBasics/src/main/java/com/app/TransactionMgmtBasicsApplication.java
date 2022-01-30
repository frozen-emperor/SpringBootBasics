package com.app;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@SpringBootApplication
public class TransactionMgmtBasicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionMgmtBasicsApplication.class, args);
	}
	
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
	    DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
	    dataSourceTransactionManager.setDataSource(dataSource);

	    return dataSourceTransactionManager;
	}

}
