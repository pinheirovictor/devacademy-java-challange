package br.com.casamagalhaes.workshop.desafio.service;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import br.com.casamagalhaes.workshop.desafio.enums.StatusDoPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.repository.PedidoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import br.com.casamagalhaes.workshop.desafio.model.ItemDoPedido;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // metodo de salvar pedido
    public Pedido salvarPedido(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new NullPointerException("Pedido sem itens de produtos cadastrados");
        }
        pedido.setStatus(StatusDoPedido.PENDENTE);
        pedido.setValorProdutos(valorProdutos(pedido.getItens()));
        pedido.setValorTotal(pedido.getValorProdutos() + pedido.getTaxa());

        return pedidoRepository.saveAndFlush(pedido);
    }

    // metodo de buscar pedido
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // metodo de busca paginada dos pedidos
    public Page<Pedido> findAll(Integer paginaTamanho, Integer tamanho) {
        Pageable pageable = PageRequest.of(paginaTamanho, tamanho);
        return pedidoRepository.findAll(pageable);
    }

    // metodo de calcular valor totoal dos produtos
    private Double valorProdutos(List<ItemDoPedido> itensPedido) {
        Double valorPedido = 0.0;
        for (ItemDoPedido itemPedido : itensPedido) {
            valorPedido = valorPedido + (itemPedido.getPrecoUnitario() * itemPedido.getQuantidade());
        }
        return valorPedido;
    }

    // metodo de salvar status do pedido
    public Pedido salvarStatusDoPedido(Long id, Pedido pedido) {
        if (pedidoRepository.existsById(id)) {
            Pedido pedido2 = findById(id);

            if (pedido.getStatus().equals(StatusDoPedido.CANCELADO)
                    && !pedido2.getStatus().equals(StatusDoPedido.EM_ROTA)
                    && !pedido2.getStatus().equals(StatusDoPedido.ENTREGUE)
                    && !pedido2.getStatus().equals(StatusDoPedido.CANCELADO))
                throw new UnsupportedOperationException("Status impossivel de ser alterado!");

            if (pedido.getStatus().equals(StatusDoPedido.EM_ROTA)
                    && pedido2.getStatus().equals(StatusDoPedido.PRONTO)) {
                throw new UnsupportedOperationException(
                        "Status impossivel de ser alterado, porque o status está em PRONTO!");
            }

            if (pedido.getStatus().equals(StatusDoPedido.ENTREGUE)
                    && pedido2.getStatus().equals(StatusDoPedido.EM_ROTA)) {
                throw new UnsupportedOperationException(
                        "Status impossivel de ser alterado, porque o status atual está EM ROTA!");
            }

            pedido2.setStatus(pedido.getStatus());
            return pedidoRepository.saveAndFlush(pedido2);
        } else {
            throw new EntityNotFoundException("Pedido nao cadastrado!");
        }

    }

    // metodo de deletar pedido
    public void deletePedido(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        } else
            throw new EntityNotFoundException("Pedido id: " + id);
    }

    // metodo de atualizar pedido
    public Pedido atualizarPedido(Long id, Pedido pedido) {
        if (pedidoRepository.existsById(id)) {
            Pedido pedido2 = findById(id);
            if (!(pedido.getStatus() == pedido2.getStatus())) {
                throw new UnsupportedOperationException("status do pedido errado!");
            }
            if (!(id == pedido.getId())) {
                throw new UnsupportedOperationException("id errado!");
            }
            return pedidoRepository.saveAndFlush(pedido);
        }
        return pedido;
    }

}
