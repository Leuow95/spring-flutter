package com.example.spring_flutter.domain.pessoa;

import org.springframework.web.multipart.MultipartFile;

public record PessoaRequestDto(
        String nome,
        String cpf,
        MultipartFile photo
) {
}
