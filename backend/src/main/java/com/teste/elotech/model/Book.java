package com.teste.elotech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "titulo", nullable = false)
    private String title;

    @NotNull
    @NotEmpty
    @Column(name = "autor", nullable = false)
    private String author;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String isbn;

    @NotNull
    @PastOrPresent
    @Column(name = "data_publicacao", nullable = false)
    private LocalDate publishedAt;

    @NotNull
    @NotEmpty
    @Column(name = "categoria", nullable = false)
    private String category;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Loan> loans;
}
