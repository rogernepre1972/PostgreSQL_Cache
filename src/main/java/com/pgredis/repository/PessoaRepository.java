package com.pgredis.repository;

import com.pgredis.model.postgres.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    Optional<Pessoa> findById(Integer id);

    @Query("SELECT p FROM Pessoa p")
    List<Pessoa> findAll();

    void deleteById(Integer id);
}
