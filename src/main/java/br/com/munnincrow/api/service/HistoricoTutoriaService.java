package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.HistoricoTutoriaResponse;
import br.com.munnincrow.api.model.HistoricoTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.repository.HistoricoTutoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricoTutoriaService {

    private final HistoricoTutoriaRepository repository;

    public HistoricoTutoriaService(HistoricoTutoriaRepository repository) {
        this.repository = repository;
    }

    public void registrar(SolicitacaoTutoria solicitacao, Long usuarioId, String mensagem) {
        HistoricoTutoria h = new HistoricoTutoria();
        h.setSolicitacao(solicitacao);
        h.setUsuarioId(usuarioId);
        h.setMensagem(mensagem);
        repository.save(h);
    }

    public List<HistoricoTutoriaResponse> listar(Long solicitacaoId) {
        return repository.findBySolicitacaoIdOrderByDataAsc(solicitacaoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<HistoricoTutoriaResponse> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdOrderByDataDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private HistoricoTutoriaResponse toResponse(HistoricoTutoria h) {
        HistoricoTutoriaResponse resp = new HistoricoTutoriaResponse();
        resp.id = h.getId();
        resp.mensagem = h.getMensagem();
        resp.data = h.getData();
        resp.usuarioId = h.getUsuarioId();
        return resp;
    }
}