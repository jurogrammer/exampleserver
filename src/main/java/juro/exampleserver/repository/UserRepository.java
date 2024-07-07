package juro.exampleserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import juro.exampleserver.repository.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserById(long id);

	Optional<User> findByUsername(String username);
}
