package com.theismann.loginAndReg.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.theismann.loginAndReg.models.User;

public interface UserRepository extends CrudRepository<User, Long>{
	List<User> findAll();
	User findByEmail(String email);

}
