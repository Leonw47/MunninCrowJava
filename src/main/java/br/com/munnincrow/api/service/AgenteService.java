package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Agente;
import br.com.munnincrow.api.repository.AgenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenteService {

    private final AgenteRepository repository;

    public AgenteService(AgenteRepository repository) {
        this.repository = repository;
    }

    public List<Agente> listar() {
        return repository.findAll();
    }

    public Agente salvar(Agente agente) {
        return repository.save(agente);
    }

    public Agente buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Agente atualizar(Long id, Agente dados) {
        return repository.findById(id).map(agente -> {
            agente.setNome(dados.getNome());
            agente.setTipo(dados.getTipo());
            agente.setDescricao(dados.getDescricao());
            return repository.save(agente);
        }).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}