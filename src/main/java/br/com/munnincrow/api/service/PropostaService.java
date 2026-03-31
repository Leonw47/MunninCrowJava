package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.PropostaCreateDTO;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropostaService {

    private final PropostaRepository repository;

    public PropostaService(PropostaRepository repository) {
        this.repository = repository;
    }

    public Proposta buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Proposta> listar() {
        return repository.findAll();
    }

    public List<Proposta> listarPorEdital(Long editalId) {
        return repository.findByEditalId(editalId);
    }

    public List<Proposta> listarPorUsuario(Long userId) {
        return repository.findByCriadoPorId(userId);
    }

    public Proposta criar(PropostaCreateDTO dto, User user, Edital edital) {
        Proposta p = new Proposta();
        p.setTitulo(dto.titulo);
        p.setConteudo(dto.conteudo);
        p.setCriadoPor(user);
        p.setEdital(edital);
        p.setDataCriacao(LocalDateTime.now());
        return repository.save(p);
    }

    public Proposta atualizar(Long id, Proposta dados) {
        return repository.findById(id).map(p -> {
            p.setTitulo(dados.getTitulo());
            p.setConteudo(dados.getConteudo());
            return repository.save(p);
        }).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}