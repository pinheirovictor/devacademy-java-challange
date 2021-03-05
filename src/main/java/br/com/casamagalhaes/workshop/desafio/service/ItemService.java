package br.com.casamagalhaes.workshop.desafio.service;

import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.casamagalhaes.workshop.desafio.model.ItemDoPedido;
import br.com.casamagalhaes.workshop.desafio.repository.ItemPedidoRepository;

@Service
public class ItemService {

    @Autowired
    private ItemPedidoRepository itemDoPedidoRepository;

    // metodo de atualizar item do pedido/ salvar item no pedido
    public ItemDoPedido atualizarItemdoPedido(Long id, ItemDoPedido item) {
        if (itemDoPedidoRepository.existsById(id)) {
            return itemDoPedidoRepository.saveAndFlush(item);
        } else {
            throw new EntityNotFoundException("ERRO");
        }

    }
}