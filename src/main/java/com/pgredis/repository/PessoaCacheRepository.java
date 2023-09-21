package com.pgredis.repository;

import com.pgredis.model.postgres.Pessoa;
import com.pgredis.model.redis.PessoaCache;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaCacheRepository extends CrudRepository<PessoaCache, Integer> {

    Optional<PessoaCache> findById(Integer id);

    boolean findById(boolean b);

    List<PessoaCache> findAll();
}
