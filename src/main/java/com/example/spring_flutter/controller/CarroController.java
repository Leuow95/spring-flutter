package com.example.spring_flutter.controller;

import com.example.spring_flutter.domain.carro.Carro;
import com.example.spring_flutter.domain.carro.CarroRequestDto;
import com.example.spring_flutter.services.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/carro/v1")
public class CarroController {
    @Autowired
    private CarroService carroService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Carro> create(
            @RequestParam("pessoaId") UUID pessoaId,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("placa") String placa,
            @RequestParam("foto") MultipartFile foto
    ){
        CarroRequestDto carroRequestDto = new CarroRequestDto(marca, placa, modelo, foto);
        Carro newCarro = carroService.create(pessoaId, carroRequestDto);

        return ResponseEntity.ok(newCarro);
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<List<Carro>> getCarrosByPessoaId(
            @PathVariable UUID pessoaId){
        List<Carro> carros = carroService.getCarrosByPessoaId(pessoaId);
        return ResponseEntity.ok(carros);
    }

    @PatchMapping("/{carroId}")
    public ResponseEntity<Carro> updateCarro(
            @PathVariable UUID carroId,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo,
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "foto", required = false) MultipartFile foto
    ){


        CarroRequestDto carroRequestDto = new CarroRequestDto(marca, modelo, placa, foto);

        Carro updatedCarro = carroService.updateCarro(carroRequestDto, carroId);

        return ResponseEntity.ok(updatedCarro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable UUID id
    ){
        carroService.deleteCarro(id);
        return ResponseEntity.ok("Carro com o id" + id + "deletado com sucesso");

    }
}
