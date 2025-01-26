package com.teste.elotech.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teste.elotech.enums.LoanStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate loanDate;

    @NotNull
    @Column(name = "data_devolucao", nullable = false)
    private LocalDate devolutionDate;

    @NotNull
    @Column(name = "status", nullable = false)
    private LoanStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "livro_id")
    @JsonBackReference
    private Book book;
}
