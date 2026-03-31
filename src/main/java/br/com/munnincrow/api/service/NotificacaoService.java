package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.Notificacao;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.EditalRepository;
import br.com.munnincrow.api.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository repo;
    private final EditalRepository editalRepo;
    private final RecomendacaoEditalService recomendacaoService;

    public NotificacaoService(NotificacaoRepository repo, EditalRepository editalRepo, RecomendacaoEditalService recomendacaoService) {
        this.repo = repo;
        this.editalRepo = editalRepo;
        this.recomendacaoService = recomendacaoService;
    }

    public void gerarNotificacoes(User usuario) {

        List<Edital> recomendados = recomendacaoService.recomendarPara(usuario);

        for (Edital e : recomendados) {

            boolean urgente = e.getDataEncerramento() != null &&
                    LocalDate.now().until(e.getDataEncerramento()).getDays() <= 5;

            boolean novo = e.getDataImportacao() != null &&
                    LocalDate.now().until(e.getDataImportacao()).getDays() >= -3;

            if (urgente) {
                criar(usuario, e, "O edital \"" + e.getTitulo() + "\" está prestes a encerrar!");
            }

            if (novo) {
                criar(usuario, e, "Novo edital relevante disponível: \"" + e.getTitulo() + "\"");
            }
        }
    }

    public List<Notificacao> listar(User usuario) {
        return repo.findByUsuarioOrderByDataCriacaoDesc(usuario);
    }

    public void marcarComoLida(Long id, User usuario) {
        Notificacao n = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada"));

        if (!n.getUsuario().getId().equals(usuario.getId())) {
            throw new SecurityException("Acesso negado");
        }

        n.setLida(true);
        repo.save(n);
    }

    private void criar(User usuario, Edital edital, String mensagem) {
        Notificacao n = new Notificacao();
        n.setUsuario(usuario);
        n.setEdital(edital);
        n.setMensagem(mensagem);
        repo.save(n);
    }
}