package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.SessaoTutoriaRequest;
import br.com.munnincrow.api.model.SessaoTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusSessao;
import br.com.munnincrow.api.repository.SessaoTutoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessaoTutoriaService {

    private final SessaoTutoriaRepository repo;
    private final SolicitacaoTutoriaService solicitacaoService;
    private final DisponibilidadeConsultorService disponibilidadeService;
    private final NotificacaoService notificacaoService;
    private final HistoricoTutoriaService historicoService;

    public SessaoTutoriaService(
            SessaoTutoriaRepository repo,
            SolicitacaoTutoriaService solicitacaoService,
            DisponibilidadeConsultorService disponibilidadeService,
            NotificacaoService notificacaoService,
            HistoricoTutoriaService historicoService
    ) {
        this.repo = repo;
        this.solicitacaoService = solicitacaoService;
        this.disponibilidadeService = disponibilidadeService;
        this.notificacaoService = notificacaoService;
        this.historicoService = historicoService;
    }

    public SessaoTutoria agendar(Long solicitacaoId, SessaoTutoriaRequest req) {

        SolicitacaoTutoria solicitacao = solicitacaoService.buscarPorId(solicitacaoId);

        User consultor = solicitacao.getConsultor();
        User empreendedor = solicitacao.getEmpreendedor();

        if (!disponibilidadeService.estaDisponivel(consultor.getId(), req.inicio)) {
            throw new IllegalStateException("Consultor indisponível nesse horário.");
        }

        boolean conflito = repo.existsByConsultorIdAndInicioLessThanEqualAndFimGreaterThanEqual(
                consultor.getId(),
                req.fim,
                req.inicio
        );

        if (conflito) {
            throw new IllegalStateException("O consultor já possui uma sessão nesse horário.");
        }

        SessaoTutoria s = new SessaoTutoria();
        s.setSolicitacao(solicitacao);
        s.setConsultor(consultor);
        s.setEmpreendedor(empreendedor);
        s.setInicio(req.inicio);
        s.setFim(req.fim);
        s.setObservacoes(req.observacoes);

        SessaoTutoria salva = repo.save(s);

        notificacaoService.criar(
                consultor.getId(),
                "Nova sessão agendada para " + req.inicio
        );

        notificacaoService.criar(
                empreendedor.getId(),
                "Sua sessão com o consultor foi agendada para " + req.inicio
        );

        historicoService.registrar(
                solicitacao,
                empreendedor.getId(),
                "Sessão agendada para " + req.inicio
        );

        return salva;
    }

    public void cancelar(Long sessaoId, Long usuarioId) {
        SessaoTutoria s = repo.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada."));

        s.setStatus(StatusSessao.CANCELADA);
        repo.save(s);

        notificacaoService.criar(
                s.getConsultor().getId(),
                "Sessão cancelada."
        );

        notificacaoService.criar(
                s.getEmpreendedor().getId(),
                "Sessão cancelada."
        );

        historicoService.registrar(
                s.getSolicitacao(),
                usuarioId,
                "Sessão cancelada."
        );
    }

    // -----------------------------
    // MÉTODOS NOVOS PARA DASHBOARD
    // -----------------------------

    public List<SessaoTutoria> listarProximasPorEmpreendedor(Long empreendedorId) {
        return repo.findByEmpreendedorIdAndInicioAfterOrderByInicioAsc(
                empreendedorId,
                LocalDateTime.now()
        );
    }

    public List<SessaoTutoria> listarProximasPorConsultor(Long consultorId) {
        return repo.findByConsultorIdAndInicioAfterOrderByInicioAsc(
                consultorId,
                LocalDateTime.now()
        );
    }
}