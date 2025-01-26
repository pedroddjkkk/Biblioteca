package com.teste.elotech.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateBookDTO {
    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String author;

    @NotNull
    @NotEmpty
    private String isbn;

    @NotNull
    @PastOrPresent
    private LocalDate publishedAt;

    @NotNull
    private String category;
}
