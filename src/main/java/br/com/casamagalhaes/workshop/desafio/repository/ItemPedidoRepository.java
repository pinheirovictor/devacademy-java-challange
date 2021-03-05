package br.com.casamagalhaes.workshop.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.casamagalhaes.workshop.desafio.model.ItemDoPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemDoPedido, Long> {

}
