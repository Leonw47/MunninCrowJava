package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.PagamentoRequest;
import br.com.munnincrow.api.model.PagamentoTutoria;
import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusPagamento;
import br.com.munnincrow.api.repository.PagamentoTutoriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagamentoTutoriaService {

    private final PagamentoTutoriaRepository repo;
    private final PropostaTutoriaService propostaService;
    private final NotificacaoService notificacaoService;
    private final HistoricoTutoriaService historicoService;

    public PagamentoTutoriaService(
            PagamentoTutoriaRepository repo,
            PropostaTutoriaService propostaService,
            NotificacaoService notificacaoService,
            HistoricoTutoriaService historicoService
    ) {
        this.repo = repo;
        this.propostaService = propostaService;
        this.notificacaoService = notificacaoService;
        this.historicoService = historicoService;
    }

    public PagamentoTutoria iniciarPagamento(Long propostaId, PagamentoRequest req) {

        PropostaTutoria proposta = propostaService.buscarPorId(propostaId);

        PagamentoTutoria pagamento = new PagamentoTutoria();
        pagamento.setProposta(proposta);
        pagamento.setEmpreendedor(proposta.getSolicitacao().getEmpreendedor());
        pagamento.setConsultor(proposta.getAutor());
        pagamento.setValor(proposta.getValor());

        PagamentoTutoria salvo = repo.save(pagamento);

        historicoService.registrar(
                proposta.getSolicitacao(),
                pagamento.getEmpreendedor().getId(),
                "Pagamento iniciado no valor de R$ " + pagamento.getValor()
        );

        return salvo;
    }

    public PagamentoTutoria confirmarPagamento(Long pagamentoId) {

        PagamentoTutoria p = repo.findById(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        p.setStatus(StatusPagamento.APROVADO);
        p.setDataConfirmacao(LocalDateTime.now());

        PagamentoTutoria salvo = repo.save(p);

        notificacaoService.criar(
                p.getConsultor().getId(),
                "Pagamento aprovado para a tutoria."
        );

        notificacaoService.criar(
                p.getEmpreendedor().getId(),
                "Seu pagamento foi confirmado."
        );

        historicoService.registrar(
                p.getProposta().getSolicitacao(),
                p.getEmpreendedor().getId(),
                "Pagamento confirmado."
        );

        return salvo;
    }

    // -----------------------------------------
    // MÉTODOS NOVOS PARA O DASHBOARD
    // -----------------------------------------

    public List<PagamentoTutoria> listarPorEmpreendedor(Long empreendedorId) {
        return repo.findByEmpreendedorIdOrderByDataCriacaoDesc(empreendedorId);
    }

    public List<PagamentoTutoria> listarPorConsultor(Long consultorId) {
        return repo.findByConsultorIdOrderByDataCriacaoDesc(consultorId);
    }
}