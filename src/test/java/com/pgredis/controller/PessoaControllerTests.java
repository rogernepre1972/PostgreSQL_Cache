package com.pgredis.controller;

import com.pgredis.model.postgres.Pessoa;
import com.pgredis.model.redis.PessoaCache;
import com.pgredis.repository.PessoaCacheRepository;
import com.pgredis.repository.PessoaRepository;
import com.pgredis.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PessoaControllerTests {

    private PessoaService pessoaService;
    private MockMvc mockMvc;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaCacheRepository pessoaCacheRepository;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
    }

    @Test
    public void testAdicionarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa(1, "Nome", "123");
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        RequestBuilder requestBuilder = post("/pessoas/adicionar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"nome\":\"Nome\",\"codigo\":\"123\"}");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andExpect(status().isOk());
        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
        verify(pessoaCacheRepository, times(1)).save(any(PessoaCache.class));
    }

    @Test
    public void testListarPessoas() throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        RequestBuilder requestBuilder = get("/pessoas/listar");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andExpect(status().isOk());
        verify(pessoaRepository, times(1)).findAll();
        verify(pessoaCacheRepository, times(1)).findAll();
    }

    @Test
    public void testBuscarPessoa() throws Exception {
        PessoaCache pessoaCache = new PessoaCache(1, "Nome", "123");
        Optional<PessoaCache> cachedPessoa = Optional.of(pessoaCache);
        when(pessoaCacheRepository.findById(1)).thenReturn(cachedPessoa);
        when(pessoaRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = get("/pessoas/buscar/1");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andExpect(status().isOk());
        verify(pessoaCacheRepository, times(1)).findById(1);
        verify(pessoaRepository, times(1)).findById(1);
    }

    @Test
    public void testDeletarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa(1, "Nome", "123");
        Optional<Pessoa> pessoaOptional = Optional.of(pessoa);
        when(pessoaRepository.findById(1)).thenReturn(pessoaOptional);
        when(pessoaCacheRepository.findById(1)).thenReturn(Optional.of(new PessoaCache(1, "Nome", "123")));

        RequestBuilder requestBuilder = delete("/pessoas/deletar/1");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andExpect(status().isOk());
        verify(pessoaRepository, times(1)).deleteById(1);
        verify(pessoaCacheRepository, times(1)).delete(any(PessoaCache.class));
    }

    @Test
    public void testAtualizarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa(1, "Nome", "123");
        Optional<Pessoa> pessoaOptional = Optional.of(pessoa);
        when(pessoaRepository.findById(1)).thenReturn(pessoaOptional);
        when(pessoaCacheRepository.findById(1)).thenReturn(Optional.of(new PessoaCache(1, "Nome", "123")));

        RequestBuilder requestBuilder = put("/pessoas/atualizar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"nome\":\"NovoNome\",\"codigo\":\"456\"}");

        ResultActions result = mockMvc.perform(requestBuilder);
        result.andExpect(status().isOk());
        verify(pessoaRepository, times(1)).findById(1);
        verify(pessoaCacheRepository, times(1)).findById(1);
        verify(pessoaCacheRepository, times(1)).save(any(PessoaCache.class));
    }

}
