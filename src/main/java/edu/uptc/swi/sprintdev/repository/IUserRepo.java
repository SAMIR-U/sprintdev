package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User persistence operations.
 */
public interface IUserRepo extends JpaRepository<User, Integer> {
    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return an optional containing the user if found
     */
    @Query("from User u WHERE u.userName = :username")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * Searches users by username prefix in a case-insensitive manner.
     *
     * @param keyword the prefix to match against usernames
     * @return a list of users whose usernames start with the provided keyword
     */
    @Query("SELECT u from User u WHERE LOWER(u.userName) LIKE LOWER(CONCAT(:keyword, '%')) ")
    List<User> findUserByKeyWord(@Param("keyword") String keyword);
}
