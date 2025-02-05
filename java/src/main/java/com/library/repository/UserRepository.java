package com.library.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.enity.User;

public interface UserRepository extends JpaRepository<User,Long>{

	Optional<User> findByName(String uname);
	Optional<User> findById(Long userId); 

}
