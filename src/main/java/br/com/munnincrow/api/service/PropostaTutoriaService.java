package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.NegociacaoTutoriaResponse;
import br.com.munnincrow.api.dto.PermissoesNegociacao;
import br.com.munnincrow.api.dto.PropostaTutoriaResponse;
import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import br.com.munnincrow.api.repository.PropostaTutoriaRepository;
import br.com.munnincrow.api.spec.PropostaTutoriaSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropostaTutoriaService {

    private final PropostaTutoriaRepository repository;
    private final SolicitacaoTutoriaService solicitacaoService;
    private final UserService userService;
    private final HistoricoTutoriaService historicoService;
    private final NotificacaoService notificacaoService;

    public PropostaTutoriaService(
            PropostaTutoriaRepository repository,
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

    public SolicitacaoTutoria buscarSolicitacao(Long id) {
        return solicitacaoService.buscarPorId(id);
    }

    public PropostaTutoria buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Page<PropostaTutoria> buscarFiltrado(
            StatusPropostaTutoria status,
            Long autorId,
            Long solicitacaoId,
            Pageable pageable
    ) {
        Specification<PropostaTutoria> spec = Specification.where((Specification<PropostaTutoria>) null);

        spec = spec.and(PropostaTutoriaSpec.status(status));
        spec = spec.and(PropostaTutoriaSpec.autor(autorId));
        spec = spec.and(PropostaTutoriaSpec.solicitacao(solicitacaoId));

        return repository.findAll(spec, pageable);
    }

    public PropostaTutoria criar(PropostaTutoria proposta, Long solicitacaoId, Long autorId) {

        SolicitacaoTutoria solicitacao = solicitacaoService.buscarPorId(solicitacaoId);
        if (solicitacao == null) {
            throw new IllegalArgumentException("Solicitação não encontrada.");
        }

        if (solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA ||
                solicitacao.getStatus() == StatusSolicitacaoTutoria.CANCELADA ||
                solicitacao.getStatus() == StatusSolicitacaoTutoria.EXPIRADA) {
            throw new IllegalStateException("Não é possível enviar propostas para esta solicitação.");
        }

        User autor = userService.buscarPorId(autorId);
        if (autor == null) {
            throw new IllegalArgumentException("Autor não encontrado.");
        }

        proposta.setSolicitacao(solicitacao);
        proposta.setAutor(autor);

        if (solicitacao.getStatus() == StatusSolicitacaoTutoria.ABERTA) {
            solicitacao.setStatus(StatusSolicitacaoTutoria.EM_NEGOCIACAO);
            solicitacaoService.salvar(solicitacao);
        }

        PropostaTutoria salva = repository.save(proposta);

        notificacaoService.criar(
                solicitacao.getEmpreendedor().getId(),
                "Você recebeu uma nova proposta na solicitação #" + solicitacao.getId()
        );

        historicoService.registrar(
                solicitacao,
                autorId,
                "Proposta enviada pelo consultor."
        );

        return salva;
    }

    public List<PropostaTutoria> listarPorSolicitacao(Long solicitacaoId) {
        return repository.findBySolicitacaoIdOrderByDataCriacaoAsc(solicitacaoId);
    }

    public PropostaTutoria atualizarStatus(Long id, StatusPropostaTutoria status) {

        PropostaTutoria proposta = repository.findById(id).orElse(null);
        if (proposta == null) {
            return null;
        }

        SolicitacaoTutoria solicitacao = proposta.getSolicitacao();

        if (solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA ||
                solicitacao.getStatus() == StatusSolicitacaoTutoria.CANCELADA ||
                solicitacao.getStatus() == StatusSolicitacaoTutoria.EXPIRADA) {
            throw new IllegalStateException("Não é possível alterar o status desta proposta.");
        }

        proposta.setStatus(status);

        if (status == StatusPropostaTutoria.ACEITA) {

            solicitacao.setStatus(StatusSolicitacaoTutoria.FECHADA);
            solicitacaoService.salvar(solicitacao);

            notificacaoService.criar(
                    proposta.getAutor().getId(),
                    "Sua proposta na solicitação #" + solicitacao.getId() + " foi aceita!"
            );

            historicoService.registrar(
                    solicitacao,
                    solicitacao.getEmpreendedor().getId(),
                    "Proposta aceita pelo empreendedor."
            );
        }

        if (status == StatusPropostaTutoria.RECUSADA) {

            notificacaoService.criar(
                    proposta.getAutor().getId(),
                    "Sua proposta na solicitação #" + solicitacao.getId() + " foi recusada."
            );

            historicoService.registrar(
                    solicitacao,
                    proposta.getAutor().getId(),
                    "Proposta recusada pelo consultor."
            );
        }

        return repository.save(proposta);
    }

    public NegociacaoTutoriaResponse montarNegociacao(Long solicitacaoId, Long usuarioId) {

        SolicitacaoTutoria solicitacao = solicitacaoService.buscarPorId(solicitacaoId);
        if (solicitacao == null) return null;

        List<PropostaTutoria> propostas = listarPorSolicitacao(solicitacaoId);

        NegociacaoTutoriaResponse resp = new NegociacaoTutoriaResponse();
        resp.solicitacao = solicitacaoService.toResponse(solicitacao);
        resp.propostas = propostas.stream().map(this::toResponse).collect(Collectors.toList());

        PermissoesNegociacao perms = new PermissoesNegociacao();
        perms.usuarioId = usuarioId;
        perms.solicitacaoFechada = solicitacao.getStatus() == StatusSolicitacaoTutoria.FECHADA;

        if (!perms.solicitacaoFechada) {
            if (solicitacao.getTipo() == TipoSolicitacao.ABERTA) {
                perms.podeEnviarProposta = true;
            } else {
                perms.podeEnviarProposta = solicitacao.getConsultor() != null &&
                        solicitacao.getConsultor().getId().equals(usuarioId);
            }
        }

        perms.podeAceitarProposta = solicitacao.getEmpreendedor().getId().equals(usuarioId)
                && !perms.solicitacaoFechada;

        perms.podeRecusarPropriaProposta = propostas.stream()
                .anyMatch(p -> p.getAutor().getId().equals(usuarioId));

        resp.permissoes = perms;

        return resp;
    }

    private PropostaTutoriaResponse toResponse(PropostaTutoria p) {
        PropostaTutoriaResponse resp = new PropostaTutoriaResponse();
        resp.id = p.getId();
        resp.valor = p.getValor();
        resp.descricao = p.getDescricao();
        resp.dataCriacao = p.getDataCriacao();
        resp.status = p.getStatus().name();
        resp.autorId = p.getAutor().getId();
        resp.solicitacaoId = p.getSolicitacao().getId();
        return resp;
    }

    // -----------------------------------------
    // MÉTODOS NOVOS PARA O DASHBOARD
    // -----------------------------------------

    public List<PropostaTutoria> listarPropostasRecebidas(Long empreendedorId) {
        return repository.findBySolicitacaoEmpreendedorIdOrderByDataCriacaoDesc(empreendedorId);
    }

    public List<PropostaTutoria> listarPorAutor(Long autorId) {
        return repository.findByAutorIdOrderByDataCriacaoDesc(autorId);
    }
}