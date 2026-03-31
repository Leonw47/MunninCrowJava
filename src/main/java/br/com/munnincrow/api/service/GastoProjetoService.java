package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.AcompanhamentoProjeto;
import br.com.munnincrow.api.model.GastoProjeto;
import br.com.munnincrow.api.repository.GastoProjetoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GastoProjetoService {

    private final GastoProjetoRepository repository;

    public GastoProjetoService(GastoProjetoRepository repository) {
        this.repository = repository;
    }

    public List<GastoProjeto> listar() {
        return repository.findAll();
    }

    public GastoProjeto salvar(GastoProjeto gasto) {
        return repository.save(gasto);
    }

    public GastoProjeto buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public GastoProjeto atualizar(Long id, GastoProjeto dados) {
        return repository.findById(id).map(gasto -> {
            gasto.setCategoria(dados.getCategoria());
            gasto.setValor(dados.getValor());
            gasto.setData(dados.getData());
            gasto.setDescricao(dados.getDescricao());
            return repository.save(gasto);
        }).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}