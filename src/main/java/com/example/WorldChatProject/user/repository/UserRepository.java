package com.example.WorldChatProject.user.repository;

import com.example.WorldChatProject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUserName(String userName);
	Optional<User> findByUserNickName(String userNickName);
}
