package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.HistoricoInteracaoResponse;
import br.com.munnincrow.api.model.InteracaoEdital;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.InteracaoEditalRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class HistoricoInteracaoService {

    private final InteracaoEditalRepository repo;

    public HistoricoInteracaoService(InteracaoEditalRepository repo) {
        this.repo = repo;
    }

    public List<HistoricoInteracaoResponse> listarHistorico(User usuario) {

        return repo.findByUsuario(usuario)
                .stream()
                .sorted(Comparator.comparing(InteracaoEdital::getData).reversed())
                .map(i -> {
                    HistoricoInteracaoResponse r = new HistoricoInteracaoResponse();
                    r.editalId = i.getEdital().getId();
                    r.titulo = i.getEdital().getTitulo();
                    r.clicou = i.isClicou();
                    r.favoritou = i.isFavoritou();
                    r.seInscreveu = i.isSeInscreveu();
                    r.data = i.getData();
                    return r;
                })
                .toList();
    }
}