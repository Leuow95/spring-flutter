package com.example.spring_flutter.domain.carro;

import org.springframework.web.multipart.MultipartFile;

public record CarroRequestDto(
        String marca,
        String placa,
        String modelo,
        MultipartFile foto
) {
}
