package com.example.spring_flutter.repositories;

import com.example.spring_flutter.domain.carro.Carro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CarroRepository extends CrudRepository<Carro, UUID> {
    @Query("SELECT carro FROM Carro carro WHERE carro.pessoa.id = :pessoaId")
    List<Carro> getCarrosByPessoaId(@Param("pessoaId") UUID pessoaId);
}
