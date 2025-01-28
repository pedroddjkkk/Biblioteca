package com.teste.elotech.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "usuario_id")
    @JsonManagedReference
    private User user;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "livro_id")
    @JsonManagedReference
    private Book book;
}
