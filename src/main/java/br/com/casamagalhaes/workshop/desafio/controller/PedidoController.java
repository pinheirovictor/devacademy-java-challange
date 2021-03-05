package br.com.casamagalhaes.workshop.desafio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.casamagalhaes.workshop.desafio.exception.Exception;
import br.com.casamagalhaes.workshop.desafio.model.ItemDoPedido;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.service.ItemService;
import br.com.casamagalhaes.workshop.desafio.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedido de Venda")
@RestController
@RequestMapping(path = "/api/v1/pedidos")
public class PedidoController extends Exception {

    @Autowired
    private PedidoService service;

    @Autowired
    private ItemService itemService;

    // metodo de salvar pedido
    @PostMapping({ "/", "" })
    public Pedido salvarPedido(@Valid @RequestBody Pedido pedido) {
        Pedido pedidoNovo = service.salvarPedido(pedido);
        for (ItemDoPedido element : pedidoNovo.getItens()) {
            element.setPedido(pedidoNovo);
            itemService.atualizarItemdoPedido(element.getId(), element);
        }
        return pedidoNovo;
    }

    // metodo de busca paginada
    @GetMapping({ "/", "" })
    public Page<Pedido> findAll(@RequestParam Integer pagina, @RequestParam Integer tamanho) {
        return service.findAll(pagina, tamanho);
    }

    // metodo de busca por id
    @GetMapping({ "/{id}" })
    public Pedido findById(@PathVariable Long id) {
        return service.findById(id);
    }

    // metodo de salvar status do pedido
    @PostMapping({ "/{id}/status", "{id}/status" })
    public Pedido salvarStatusDoPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.salvarStatusDoPedido(id, pedido);
    }

    // metodo atualizar pedido
    @PutMapping({ "/{id}" })
    public Pedido atualizarPedio(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.atualizarPedido(id, pedido);
    }

    // metodo de deletar pedido
    @DeleteMapping("/{id}")
    public void deletarPedido(@PathVariable Long id) {
        service.deletePedido(id);
    }
}
