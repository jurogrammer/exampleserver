package juro.exampleserver.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import juro.exampleserver.repository.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsById(long id);

	Optional<User> findUserById(long id);

	Optional<User> findByUsername(String username);
}
