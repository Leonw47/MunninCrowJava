package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.AlertaProposta;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.repository.AlertaPropostaRepository;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaPropostaService {

    private final AlertaPropostaRepository alertaRepo;
    private final PropostaRepository propostaRepo;
    private final AcompanhamentoPropostaService acompanhamentoService;
    private final EmailNotificationService emailService;

    public AlertaPropostaService(
            AlertaPropostaRepository alertaRepo,
            PropostaRepository propostaRepo,
            AcompanhamentoPropostaService acompanhamentoService,
            EmailNotificationService emailService
    ) {
        this.alertaRepo = alertaRepo;
        this.propostaRepo = propostaRepo;
        this.acompanhamentoService = acompanhamentoService;
        this.emailService = emailService;
    }

    public void verificarPrazos() {
        List<Proposta> propostas = propostaRepo.findAll();
        LocalDate hoje = LocalDate.now();

        for (Proposta p : propostas) {
            LocalDate encerramento = p.getEdital().getDataEncerramento();
            if (encerramento == null) continue;

            if (encerramento.minusDays(7).isEqual(hoje)) {
                gerarAlerta(p, "prazo_proximo",
                        "Faltam 7 dias para o encerramento do edital.");
            }

            if (encerramento.minusDays(1).isEqual(hoje)) {
                gerarAlerta(p, "prazo_proximo",
                        "Último dia para enviar a proposta!");
            }

            if (hoje.isAfter(encerramento)) {
                gerarAlerta(p, "edital_fechado",
                        "O edital foi encerrado.");
            }
        }
    }

    public AlertaProposta gerarAlerta(Proposta proposta, String tipo, String mensagem) {
        AlertaProposta alerta = new AlertaProposta();
        alerta.setProposta(proposta);
        alerta.setTipo(tipo);
        alerta.setMensagem(mensagem);
        alerta.setDataGeracao(LocalDateTime.now());
        alerta.setLido(false);

        alertaRepo.save(alerta);

        acompanhamentoService.registrarEvento(
                acompanhamentoService.iniciarAcompanhamento(proposta),
                "alerta",
                mensagem
        );

        emailService.enviar(
                proposta.getUsuario().getEmail(),
                "Alerta sobre sua proposta",
                mensagem
        );

        return alerta;
    }

    public List<AlertaProposta> listarAlertas(Long propostaId) {
        Proposta p = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        return alertaRepo.findByPropostaOrderByDataGeracaoDesc(p);
    }

    public void marcarComoLido(Long alertaId) {
        AlertaProposta alerta = alertaRepo.findById(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta não encontrado."));
        alerta.setLido(true);
        alertaRepo.save(alerta);
    }
}