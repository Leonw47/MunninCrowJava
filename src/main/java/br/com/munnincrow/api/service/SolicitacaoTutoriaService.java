package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.SolicitacaoTutoriaResponse;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import br.com.munnincrow.api.repository.SolicitacaoTutoriaRepository;
import br.com.munnincrow.api.spec.SolicitacaoTutoriaSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoTutoriaService {

    private final SolicitacaoTutoriaRepository repository;
    private final UserService userService;
    private final HistoricoTutoriaService historicoService;
    private final NotificacaoService notificacaoService;

    public SolicitacaoTutoriaService(
            SolicitacaoTutoriaRepository repository,
            UserService userService,
            HistoricoTutoriaService historicoService,
            NotificacaoService notificacaoService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.historicoService = historicoService;
        this.notificacaoService = notificacaoService;
    }

    public List<SolicitacaoTutoria> listar() {
        return repository.findAll();
    }

    public SolicitacaoTutoria salvar(SolicitacaoTutoria solicitacao) {

        User empreendedor = userService.buscarPorId(solicitacao.getEmpreendedor().getId());
        if (empreendedor == null) {
            throw new IllegalArgumentException("Empreendedor não encontrado.");
        }

        solicitacao.setEmpreendedor(empreendedor);

        if (solicitacao.getConsultor() != null) {
            User consultor = userService.buscarPorId(solicitacao.getConsultor().getId());
            if (consultor == null) {
                throw new IllegalArgumentException("Consultor não encontrado.");
            }
            solicitacao.setConsultor(consultor);
        }

        if (solicitacao.getTipo() == null) {
            solicitacao.setTipo(TipoSolicitacao.ABERTA);
        }

        SolicitacaoTutoria salvo = repository.save(solicitacao);

        historicoService.registrar(
                salvo,
                salvo.getEmpreendedor().getId(),
                "Solicitação criada pelo empreendedor."
        );

        return salvo;
    }

    public SolicitacaoTutoria buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<SolicitacaoTutoria> buscarFiltrado(
            StatusSolicitacaoTutoria status,
            TipoSolicitacao tipo,
            Long empreendedorId,
            Long consultorId,
            String texto,
            Pageable pageable
    ) {
        Specification<SolicitacaoTutoria> spec = Specification.where((Specification<SolicitacaoTutoria>) null);

        spec = spec.and(SolicitacaoTutoriaSpec.status(status));
        spec = spec.and(SolicitacaoTutoriaSpec.tipo(tipo));
        spec = spec.and(SolicitacaoTutoriaSpec.empreendedor(empreendedorId));
        spec = spec.and(SolicitacaoTutoriaSpec.consultor(consultorId));
        spec = spec.and(SolicitacaoTutoriaSpec.texto(texto));

        return repository.findAll(spec, pageable);
    }

    public SolicitacaoTutoria atualizarStatus(Long id, StatusSolicitacaoTutoria status) {
        return repository.findById(id).map(solicitacao -> {

            solicitacao.setStatus(status);
            SolicitacaoTutoria atualizado = repository.save(solicitacao);

            historicoService.registrar(
                    atualizado,
                    atualizado.getEmpreendedor().getId(),
                    "Status da solicitação alterado para: " + status
            );

            return atualizado;

        }).orElse(null);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    public SolicitacaoTutoriaResponse toResponse(SolicitacaoTutoria s) {
        SolicitacaoTutoriaResponse resp = new SolicitacaoTutoriaResponse();
        resp.id = s.getId();
        resp.tipo = s.getTipo();
        resp.status = s.getStatus();
        resp.descricao = s.getDescricao();
        resp.empreendedorId = s.getEmpreendedor().getId();
        resp.consultorId = s.getConsultor() != null ? s.getConsultor().getId() : null;
        resp.dataCriacao = s.getDataCriacao();
        return resp;
    }

    public SolicitacaoTutoria cancelar(Long id, Long usuarioId) {
        SolicitacaoTutoria solicitacao = buscarPorId(id);
        if (solicitacao == null) return null;

        if (!solicitacao.getEmpreendedor().getId().equals(usuarioId)) {
            throw new IllegalStateException("Somente o empreendedor pode cancelar a solicitação.");
        }

        if (solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA) {
            throw new IllegalStateException("A solicitação já foi fechada e não pode ser cancelada.");
        }

        solicitacao.setStatus(StatusSolicitacaoTutoria.CANCELADA);
        SolicitacaoTutoria atualizado = repository.save(solicitacao);

        historicoService.registrar(
                atualizado,
                usuarioId,
                "Solicitação cancelada pelo empreendedor."
        );

        if (solicitacao.getConsultor() != null) {
            notificacaoService.criar(
                    solicitacao.getConsultor().getId(),
                    "A solicitação #" + solicitacao.getId() + " foi cancelada pelo empreendedor."
            );
        }

        return atualizado;
    }

    public SolicitacaoTutoria reabrir(Long id, Long usuarioId) {
        SolicitacaoTutoria solicitacao = buscarPorId(id);
        if (solicitacao == null) return null;

        if (!solicitacao.getEmpreendedor().getId().equals(usuarioId)) {
            throw new IllegalStateException("Somente o empreendedor pode reabrir a solicitação.");
        }

        if (solicitacao.getStatus() != StatusSolicitacaoTutoria.CANCELADA &&
                solicitacao.getStatus() != StatusSolicitacaoTutoria.EXPIRADA) {
            throw new IllegalStateException("A solicitação só pode ser reaberta se estiver cancelada ou expirada.");
        }

        solicitacao.setStatus(StatusSolicitacaoTutoria.ABERTA);
        SolicitacaoTutoria atualizado = repository.save(solicitacao);

        historicoService.registrar(
                atualizado,
                usuarioId,
                "Solicitação reaberta pelo empreendedor."
        );

        if (solicitacao.getConsultor() != null) {
            notificacaoService.criar(
                    solicitacao.getConsultor().getId(),
                    "A solicitação #" + solicitacao.getId() + " foi reaberta."
            );
        }

        return atualizado;
    }

    public void expirarSolicitacoesVencidas(LocalDateTime agora) {
        List<SolicitacaoTutoria> abertas = repository.findAll();

        abertas.stream()
                .filter(s -> s.getStatus() != StatusSolicitacaoTutoria.FECHADA)
                .filter(s -> s.getStatus() != StatusSolicitacaoTutoria.CANCELADA)
                .filter(s -> s.getStatus() != StatusSolicitacaoTutoria.EXPIRADA)
                .filter(s -> s.getDataExpiracao() != null && s.getDataExpiracao().isBefore(agora))
                .forEach(s -> {
                    s.setStatus(StatusSolicitacaoTutoria.EXPIRADA);
                    repository.save(s);

                    historicoService.registrar(
                            s,
                            s.getEmpreendedor().getId(),
                            "Solicitação expirada automaticamente."
                    );

                    notificacaoService.criar(
                            s.getEmpreendedor().getId(),
                            "Sua solicitação #" + s.getId() + " expirou automaticamente."
                    );
                });
    }

    // -----------------------------------------
    // MÉTODOS NOVOS PARA O DASHBOARD
    // -----------------------------------------

    public List<SolicitacaoTutoria> listarPorEmpreendedor(Long empreendedorId) {
        return repository.findByEmpreendedorIdOrderByDataCriacaoDesc(empreendedorId);
    }

    public List<SolicitacaoTutoria> listarAbertasParaConsultor(Long consultorId) {
        return repository.findByStatusAndTipoAndConsultorIsNull(
                StatusSolicitacaoTutoria.ABERTA,
                TipoSolicitacao.ABERTA
        );
    }
}