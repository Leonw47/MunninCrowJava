package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Orientacao;
import br.com.munnincrow.api.repository.EditalRepository;
import br.com.munnincrow.api.repository.OrientacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrientacaoService {

    private final OrientacaoRepository repository;
    private final EditalRepository editalRepository;

    public OrientacaoService(OrientacaoRepository repository, EditalRepository editalRepository) {
        this.repository = repository;
        this.editalRepository = editalRepository;
    }

    public List<Orientacao> listarPorEdital(Long editalId) {
        return repository.findByEditalId(editalId);
    }

    public Orientacao salvar(Orientacao orientacao) {
        return repository.save(orientacao);
    }

    public Orientacao buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}