package com.pgredis.controller;

import com.pgredis.model.postgres.Pessoa;
import com.pgredis.model.redis.PessoaCache;
import com.pgredis.repository.PessoaCacheRepository;
import com.pgredis.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaCacheRepository pessoaCacheRepository;

    @PostMapping("/adicionar")
    public Pessoa adicionarPessoa(@RequestBody Pessoa pessoa) {
        Pessoa savedPessoa = pessoaRepository.save(pessoa);
        pessoaCacheRepository.save(new PessoaCache(savedPessoa.getId(), savedPessoa.getNome(), savedPessoa.getCodigo()));
        return savedPessoa;
    }

    @GetMapping("/listar")
    public Iterable<Pessoa> listarPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        List<PessoaCache> pessoaCaches = pessoaCacheRepository.findAll();
        if (pessoaCaches.size() != pessoas.size()) {
            for (Pessoa pessoa : pessoas) {
                if (pessoaCacheRepository.findById(pessoa.getId()).isEmpty()) {
                    pessoaCacheRepository.save(new PessoaCache(pessoa.getId(), pessoa.getNome(), pessoa.getCodigo()));
                }
            }
            for (PessoaCache pessoaCache : pessoaCaches) {
                if (pessoaRepository.findById(pessoaCache.getId()).isEmpty()) {
                    pessoaCacheRepository.deleteById(pessoaCache.getId());
                }
            }
        }
        return pessoas;
    }

    @GetMapping("/buscar/{codigo}")
    public Optional<Pessoa> buscarPessoa(@PathVariable String codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(Integer.parseInt(codigo));
        if (pessoaCacheRepository.findById(Integer.parseInt(codigo)).isEmpty() && pessoa.isPresent()) {
            pessoaCacheRepository.save(new PessoaCache(pessoa.get().getId(), pessoa.get().getNome(), pessoa.get().getCodigo()));
        }
        return pessoa;
    }

    @DeleteMapping("/deletar/{codigo}")
    public void deletarPessoa(@PathVariable String codigo) {
        Optional<Pessoa> pessoa = pessoaRepository.findById(Integer.parseInt(codigo));
        if (pessoa.isPresent()) {
            pessoaRepository.deleteById(Integer.parseInt(codigo));
            Optional<PessoaCache> pessoaCached = pessoaCacheRepository.findById(Integer.parseInt(codigo));
            pessoaCached.ifPresent(pessoaCache -> pessoaCacheRepository.delete(pessoaCache));
        }
    }

    @PutMapping("/atualizar/{codigo}")
    public Pessoa atualizarPessoa(@PathVariable String codigo, @RequestBody Pessoa pessoa) {

        Optional<Pessoa> pessoaAtualizada = pessoaRepository.findById(Integer.parseInt(codigo));

        if (pessoaAtualizada.isPresent()) {
            pessoaAtualizada.get().setNome(pessoa.getNome());
            pessoaAtualizada.get().setCodigo(pessoa.getCodigo());
            pessoaRepository.save(pessoaAtualizada.get());

            Optional<PessoaCache> pessoaCached = pessoaCacheRepository.findById(Integer.parseInt(codigo));
            pessoaCached.ifPresent(pessoaCache -> {
                pessoaCache.setNome(pessoa.getNome());
                pessoaCache.setCodigo(pessoa.getCodigo());
                pessoaCacheRepository.save(pessoaCache);
            });

            return pessoaRepository.save(pessoaAtualizada.get());
        }
        return null;
    }
}
