package com.example.spring_flutter.services;

import com.example.spring_flutter.domain.pessoa.PessoaRequestDto;
import com.example.spring_flutter.domain.pessoa.Pessoa;
import com.example.spring_flutter.domain.pessoa.PessoaResponseDto;
import com.example.spring_flutter.exceptions.PessoaNotFound;
import com.example.spring_flutter.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PessoaService {

    @Autowired
    private CarroService carroService;

    @Autowired
    private PessoaRepository pessoaRepository;


    public Pessoa create(PessoaRequestDto pessoaRequestDto){
        String imgUrl = null;

        imgUrl = carroService.uploadImage(pessoaRequestDto.photo());

        Pessoa newPessoa = new Pessoa();


        newPessoa.setNome(pessoaRequestDto.nome());
        newPessoa.setCpf(pessoaRequestDto.cpf());
        newPessoa.setPhotoUrl(imgUrl);

        return pessoaRepository.save(newPessoa);
    }

    public List<PessoaResponseDto> getPessoas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Pessoa> pessoaPage = this.pessoaRepository.findAll(pageable);

        return pessoaPage.map(pessoa -> new PessoaResponseDto(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getPhotoUrl()
        )).stream().toList();
    }

    public Pessoa getPessoaById(UUID id){
        return pessoaRepository.findById(id).orElseThrow(
                ()->new PessoaNotFound("Pessoa não encontrada com o id" + id));
    }

    public Pessoa updatePessoa(PessoaRequestDto pessoaRequestDto, UUID pessoaId){

        Pessoa existingPessoa = pessoaRepository.findById(pessoaId).orElseThrow(()-> new PessoaNotFound("Pessoa não encontrada"));

        if(pessoaRequestDto.nome()!=null){
            existingPessoa.setNome(pessoaRequestDto.nome());
        }
        if(pessoaRequestDto.cpf()!=null){
            existingPessoa.setCpf(existingPessoa.getCpf());
        }
        if(pessoaRequestDto.photo()!=null){
            String imageUrl = carroService.uploadImage(pessoaRequestDto.photo());
            existingPessoa.setPhotoUrl(imageUrl);
        }
        return pessoaRepository.save(existingPessoa);
    }

    public void delete(UUID id){
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(
                ()-> new PessoaNotFound("Pessoa não encontrada com o id" + id));

        pessoaRepository.delete(pessoa);
    }

}
