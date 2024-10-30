package com.example.spring_flutter.domain.pessoa;

import java.util.UUID;

public record PessoaResponseDto(
        UUID id,
        String nome,
        String cpf,
        String photoUrl
) {
}
