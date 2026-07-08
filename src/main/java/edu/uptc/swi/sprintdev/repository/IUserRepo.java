package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserRepo extends JpaRepository<User, Integer> {
    @Query("from User u WHERE u.userName = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u from User u WHERE LOWER(u.username) LIKE LOWER(CONCAT(:keyword, '%')) ")
    List<User> findUserByKeyWord(@Param("keyword") String keyword);
}
