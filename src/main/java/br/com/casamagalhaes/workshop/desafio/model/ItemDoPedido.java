package br.com.casamagalhaes.workshop.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "itens")
public class ItemDoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item")
    private Long id;

    @NotEmpty()
    private String descricao;

    @NotNull()
    private Double precoUnitario;

    @NotNull()
    @Min(value = 1)
    private Long quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Pedido pedido;
}
