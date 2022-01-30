package com.app.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.UserEntity;
import com.app.repo.UserRepo;

@RestController
public class Controller {

	@Autowired
	private UserRepo repo;

//	@Autowired
//	private DataSourceTransactionManager txMan;
	@Autowired
	private JdbcTemplate t;

	@Autowired
	private DataSourceTransactionManager txMan;

	@GetMapping("/tx")
	public String transact() {
		tx();
		return "gello";
	}

	private void tx() {
		try {
			t.getDataSource().getConnection().setAutoCommit(false);
//			System.out.println(getusers());
			System.out.println(updateUser5());
//			System.out.println(getuser5());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//DIRTY_READ_EXAMPLE
	private String updateUser5() {
		System.out.println("current:" + getuser5());
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);

		TransactionStatus status = txMan.getTransaction(def);

		try {
			String name = "shaliesh_5update";
			int id = 5;
			String sql = "update user_entity set name=? where id = ?";
			t.update(sql, name, id);
			
			//ISOLATION_READ_UNCOMMITTED will give updated but uncommited user value in
			// below statement which will be rollbacked in catch clause
			//DIRTY READ
			System.out.println("after update uncomittes:" + getuser5());
			throw new Exception("shit");
		} catch (Exception e) {
			txMan.rollback(status);
			e.printStackTrace();
		}
		
		//here since the transaction is rollbacked the original value will be visible
		//so 68 is dirty read
		System.out.println("after catch:" + getuser5());
		return getuser5().toString();
	}

	private List<UserEntity> getusers() {
		String sql = "select * from user_entity";
		List<UserEntity> users = t.query(sql, (ResultSet rs, int rowNum) -> {
			UserEntity user = new UserEntity();
			user.setId(rs.getInt("id"));
			user.setUsername(rs.getString("name"));
			return user;
		});
		return users;
	}

	private UserEntity getuser5() {

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);

		TransactionStatus status = txMan.getTransaction(def);
		UserEntity user1 = null;
		try {
			String sql = "select * from user_entity where id = ?";
			 user1 = t.queryForObject(sql, (rs, rowNum) -> {
				UserEntity user = new UserEntity();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("name"));
				return user;
			}, 5);
		} catch (Exception e) {
			txMan.rollback(status);
		}
		txMan.commit(status);
		return user1;
	}
}
