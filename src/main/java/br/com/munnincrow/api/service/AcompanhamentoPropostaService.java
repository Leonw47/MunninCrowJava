package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.AcompanhamentoProposta;
import br.com.munnincrow.api.model.EventoAcompanhamento;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.repository.AcompanhamentoPropostaRepository;
import br.com.munnincrow.api.repository.EventoAcompanhamentoRepository;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AcompanhamentoPropostaService {

    private final AcompanhamentoPropostaRepository acompRepo;
    private final EventoAcompanhamentoRepository eventoRepo;
    private final PropostaRepository propostaRepo;

    public AcompanhamentoPropostaService(
            AcompanhamentoPropostaRepository acompRepo,
            EventoAcompanhamentoRepository eventoRepo,
            PropostaRepository propostaRepo) {
        this.acompRepo = acompRepo;
        this.eventoRepo = eventoRepo;
        this.propostaRepo = propostaRepo;
    }

    public AcompanhamentoProposta iniciarAcompanhamento(Proposta proposta) {
        return acompRepo.findByProposta(proposta)
                .orElseGet(() -> {
                    AcompanhamentoProposta a = new AcompanhamentoProposta();
                    a.setProposta(proposta);
                    a.setStatusAtual(proposta.getStatus());
                    a.setUltimaAtualizacao(LocalDateTime.now());
                    return acompRepo.save(a);
                });
    }

    public AcompanhamentoProposta atualizarStatus(Long propostaId, String novoStatus) {
        Proposta proposta = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        AcompanhamentoProposta acomp = iniciarAcompanhamento(proposta);

        acomp.setStatusAtual(novoStatus);
        acomp.setUltimaAtualizacao(LocalDateTime.now());
        acompRepo.save(acomp);

        registrarEvento(acomp, "status_alterado", "Status alterado para: " + novoStatus);

        return acomp;
    }

    public void registrarEvento(AcompanhamentoProposta acomp, String tipo, String descricao) {
        EventoAcompanhamento e = new EventoAcompanhamento();
        e.setAcompanhamento(acomp);
        e.setTipo(tipo);
        e.setDescricao(descricao);
        e.setData(LocalDateTime.now());
        eventoRepo.save(e);
    }

    public List<EventoAcompanhamento> listarEventos(Long propostaId) {
        Proposta proposta = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        AcompanhamentoProposta acomp = acompRepo.findByProposta(proposta)
                .orElseThrow(() -> new IllegalArgumentException("Acompanhamento não encontrado."));

        return eventoRepo.findByAcompanhamentoOrderByDataDesc(acomp);
    }
}