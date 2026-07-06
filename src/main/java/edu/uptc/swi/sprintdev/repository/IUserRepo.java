package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUserRepo extends JpaRepository<User,Integer> {
    @Query("from User u WHERE u.username = :username")
    User findByUsername (@Param("username") String username);
}
