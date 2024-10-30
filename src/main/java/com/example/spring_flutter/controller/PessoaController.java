package com.example.spring_flutter.controller;

import com.example.spring_flutter.domain.pessoa.Pessoa;
import com.example.spring_flutter.domain.pessoa.PessoaRequestDto;
import com.example.spring_flutter.domain.pessoa.PessoaResponseDto;
import com.example.spring_flutter.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/pessoa/v1")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Pessoa> create(
            @RequestParam("nome") String nome,
            @RequestParam("cpf") String cpf,
            @RequestParam("foto") MultipartFile image){
        PessoaRequestDto pessoaRequestDto = new PessoaRequestDto(nome, cpf, image);

        Pessoa pessoa = pessoaService.create(pessoaRequestDto);

        return ResponseEntity.ok(pessoa);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDto>> getPessoas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        List<PessoaResponseDto> pessoas = pessoaService.getPessoas(page, size);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> getPessoaById(@PathVariable UUID id){
        Pessoa pessoa = pessoaService.getPessoaById(id);
        return ResponseEntity.ok(pessoa);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pessoa> update(
            @PathVariable UUID id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "foto", required = false) MultipartFile foto
    ){
        PessoaRequestDto pessoaRequestDto = new PessoaRequestDto(nome, cpf, foto);

        Pessoa updatedPessoa = pessoaService.updatePessoa(pessoaRequestDto, id);

        return ResponseEntity.ok(updatedPessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        pessoaService.delete(id);
        String responseMessage = "Usuário " + id +" deletado com sucesso";

        return ResponseEntity.ok(responseMessage);
    }


}
