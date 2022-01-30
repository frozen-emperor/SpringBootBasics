package com.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;

	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserEntity(Integer id, String username) {
		super();
		this.id = id;
		this.name = username;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return name;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", username=" + name + "]";
	}

}
