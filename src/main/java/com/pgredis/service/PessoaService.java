package com.pgredis.service;

import com.pgredis.model.postgres.Pessoa;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PessoaService {

    public PessoaService() {
    }

    public List<Pessoa> createMockPessoa(int howMany) {

        return IntStream.range(0, howMany).mapToObj(i ->
                new Pessoa(i, "Nome " + i, String.valueOf(i * 10))).collect(Collectors.toList());
    }


}
