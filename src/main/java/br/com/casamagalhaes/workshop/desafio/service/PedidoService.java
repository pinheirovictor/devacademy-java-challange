package br.com.casamagalhaes.workshop.desafio.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.repository.PedidoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import br.com.casamagalhaes.workshop.desafio.model.ItemDoPedido;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    // metodo de listagem paginada dos pedidos
    public Page<Pedido> findAll(Integer pagina, Integer tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return repository.findAll(pageable);
    }

    // metodo de listagem, retorna todos os pedidos do banco
    public Pedido findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // metodo de salvar pedido no banco
    public Pedido save(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new NullPointerException("pedido de venda sem produtos");
        }
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorTotalProdutos(valorTotalDosProdutos(pedido.getItens()));
        pedido.setValorTotal(pedido.getValorTotalProdutos() + pedido.getTaxa());

        return repository.saveAndFlush(pedido);
    }

    // metodo de calcular valor total dos pedidos
    private Double valorTotalDosProdutos(List<ItemDoPedido> itens) {
        Double valorTotal = 0.0;
        for (ItemDoPedido itemPedido : itens) {
            valorTotal = valorTotal + (itemPedido.getPrecoUnitario() * itemPedido.getQuantidade());
        }
        return valorTotal;
    }

    // metodo de atualizar pedido
    public Pedido update(Long id, Pedido pedido) {
        if (repository.existsById(id)) {
            Pedido pedidoAnterior = findById(id);

            if (pedido.getStatus() != pedidoAnterior.getStatus()) {
                throw new UnsupportedOperationException("STATUS DO PEDIDO ERRADO.");
            }
            if (!(id == pedido.getId())) {
                throw new UnsupportedOperationException("ID DO PEDIDO ERRADO.");
            }
            return repository.saveAndFlush(pedido);
        } else
            throw new EntityNotFoundException("ID PEDIDO: " + pedido.getId());

    }

    // metodo de deletar pedido
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
