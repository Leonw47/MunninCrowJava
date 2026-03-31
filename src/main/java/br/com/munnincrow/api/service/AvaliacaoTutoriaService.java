package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.AvaliacaoTutoriaRequest;
import br.com.munnincrow.api.model.AvaliacaoTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.repository.AvaliacaoTutoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoTutoriaService {

    private final AvaliacaoTutoriaRepository repository;
    private final SolicitacaoTutoriaService solicitacaoService;
    private final UserService userService;
    private final HistoricoTutoriaService historicoService;
    private final NotificacaoService notificacaoService;

    public AvaliacaoTutoriaService(
            AvaliacaoTutoriaRepository repository,
            SolicitacaoTutoriaService solicitacaoService,
            UserService userService,
            HistoricoTutoriaService historicoService,
            NotificacaoService notificacaoService
    ) {
        this.repository = repository;
        this.solicitacaoService = solicitacaoService;
        this.userService = userService;
        this.historicoService = historicoService;
        this.notificacaoService = notificacaoService;
    }

    public AvaliacaoTutoria avaliar(Long solicitacaoId, Long empreendedorId, AvaliacaoTutoriaRequest req) {

        SolicitacaoTutoria solicitacao = solicitacaoService.buscarPorId(solicitacaoId);
        if (solicitacao == null) throw new IllegalArgumentException("Solicitação não encontrada.");

        if (!solicitacao.getEmpreendedor().getId().equals(empreendedorId)) {
            throw new IllegalStateException("Somente o empreendedor pode avaliar.");
        }

        if (solicitacao.getStatus() != StatusSolicitacaoTutoria.FECHADA) {
            throw new IllegalStateException("A solicitação precisa estar fechada para ser avaliada.");
        }

        if (repository.findBySolicitacaoId(solicitacaoId).isPresent()) {
            throw new IllegalStateException("A solicitação já foi avaliada.");
        }

        User consultor = solicitacao.getConsultor();
        if (consultor == null) {
            throw new IllegalStateException("Solicitações abertas não possuem consultor definido.");
        }

        AvaliacaoTutoria av = new AvaliacaoTutoria();
        av.setSolicitacao(solicitacao);
        av.setConsultor(consultor);
        av.setNota(req.nota);
        av.setComentario(req.comentario);

        AvaliacaoTutoria salva = repository.save(av);

        int total = consultor.getTotalAvaliacoes() + 1;
        double novaMedia = ((consultor.getMediaAvaliacoes() * consultor.getTotalAvaliacoes()) + req.nota) / total;

        consultor.setTotalAvaliacoes(total);
        consultor.setMediaAvaliacoes(novaMedia);

        userService.salvar(consultor);

        historicoService.registrar(
                solicitacao,
                empreendedorId,
                "Avaliação registrada: nota " + req.nota
        );

        notificacaoService.criar(
                consultor.getId(),
                "Você recebeu uma nova avaliação na solicitação #" + solicitacao.getId()
        );

        return salva;
    }

    public List<AvaliacaoTutoria> listarPorConsultor(Long consultorId) {
        return repository.findByConsultorId(consultorId);
    }

    // -----------------------------
    // MÉTODO NOVO PARA DASHBOARD
    // -----------------------------

    public List<AvaliacaoTutoria> listarPorEmpreendedor(Long empreendedorId) {
        return repository.findBySolicitacaoEmpreendedorId(empreendedorId);
    }
}