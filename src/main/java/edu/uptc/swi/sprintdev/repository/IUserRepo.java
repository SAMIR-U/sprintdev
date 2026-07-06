package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUserRepo extends JpaRepository<User,Integer> {
    @Query("from Users u WHERE u.user_name = :username")
    User findByUsername (@Param("user_name") String username);
}
