package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.AcompanhamentoProjeto;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.AcompanhamentoProjetoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcompanhamentoProjetoService {

    private final AcompanhamentoProjetoRepository repository;
    private final UserService userService;

    public AcompanhamentoProjetoService(AcompanhamentoProjetoRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<AcompanhamentoProjeto> listar() {
        return repository.findAll();
    }

    public AcompanhamentoProjeto salvar(AcompanhamentoProjeto projeto) {

        User criador = userService.buscarPorId(projeto.getCriadoPor().getId());
        if (criador == null) {
            throw new IllegalArgumentException("Usuário criador não encontrado.");
        }

        projeto.setCriadoPor(criador);

        return repository.save(projeto);
    }

    public AcompanhamentoProjeto buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public AcompanhamentoProjeto atualizar(Long id, AcompanhamentoProjeto dados) {
        return repository.findById(id).map(projeto -> {

            projeto.setTitulo(dados.getTitulo());
            projeto.setDataInicio(dados.getDataInicio());
            projeto.setDataFim(dados.getDataFim());
            projeto.setStatus(dados.getStatus());

            return repository.save(projeto);

        }).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}