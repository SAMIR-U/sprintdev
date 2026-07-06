package edu.uptc.swi.sprintdev.repository;

import edu.uptc.swi.sprintdev.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepo extends JpaRepository<User,Integer> {
    // ya los metodos save y findByID los implementa la interfaz por eso esta clase va vacía
}
