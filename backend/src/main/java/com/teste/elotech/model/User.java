package com.teste.elotech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "nome", nullable = false)
    private String name;

    @Email
    @NotNull
    @NotEmpty
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @NotEmpty
    @Column(name = "telefone", nullable = false)
    private String telephone;

    @NotNull
    @PastOrPresent
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate registeredAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Loan> loans;
}
