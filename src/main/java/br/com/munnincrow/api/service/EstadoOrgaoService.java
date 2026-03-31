package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.EstadoOrgao;
import br.com.munnincrow.api.repository.EstadoOrgaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstadoOrgaoService {

    private final EstadoOrgaoRepository repository;

    public EstadoOrgaoService(EstadoOrgaoRepository repository) {
        this.repository = repository;
    }

    public List<EstadoOrgao> listar() {
        return repository.findAll();
    }

    public EstadoOrgao buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public EstadoOrgao salvar(EstadoOrgao eo) {
        return repository.save(eo);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    // Método essencial para importação automática
    @Transactional
    public EstadoOrgao buscarOuCriar(String estado, String orgao) {

        if (estado == null || orgao == null) {
            throw new IllegalArgumentException("Estado e órgão não podem ser nulos.");
        }

        String estadoNorm = estado.trim().toUpperCase();
        String orgaoNorm = orgao.trim();

        return repository.findByEstadoAndOrgao(estadoNorm, orgaoNorm)
                .orElseGet(() -> {
                    EstadoOrgao novo = new EstadoOrgao();
                    novo.setEstado(estadoNorm);
                    novo.setOrgao(orgaoNorm);
                    return repository.save(novo);
                });
    }
}