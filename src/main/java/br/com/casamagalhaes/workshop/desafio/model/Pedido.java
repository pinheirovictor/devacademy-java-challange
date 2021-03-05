package br.com.casamagalhaes.workshop.desafio.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import br.com.casamagalhaes.workshop.desafio.enums.StatusDoPedido;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pedido")
    private Long id;

    @Column(unique = true)
    @NotEmpty()
    private String pedido;

    @NotEmpty()
    private String nomeCliente;

    @NotEmpty()
    private String endereco;

    @NotEmpty()
    @Size(min = 7, max = 11)
    private String telefone;

    private Double valorProdutos;

    private Double taxa;

    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusDoPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemDoPedido> itens;
}
