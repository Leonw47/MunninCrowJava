package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.InteracaoEdital;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.InteracaoEditalRepository;
import org.springframework.stereotype.Service;

@Service
public class InteracaoEditalService {

    private final InteracaoEditalRepository repo;

    public InteracaoEditalService(InteracaoEditalRepository repo) {
        this.repo = repo;
    }

    public void registrarClique(User usuario, Edital edital) {
        InteracaoEdital i = new InteracaoEdital();
        i.setUsuario(usuario);
        i.setEdital(edital);
        i.setClicou(true);
        repo.save(i);
    }

    public void registrarFavorito(User usuario, Edital edital) {
        InteracaoEdital i = new InteracaoEdital();
        i.setUsuario(usuario);
        i.setEdital(edital);
        i.setFavoritou(true);
        repo.save(i);
    }

    public void registrarInscricao(User usuario, Edital edital) {
        InteracaoEdital i = new InteracaoEdital();
        i.setUsuario(usuario);
        i.setEdital(edital);
        i.setSeInscreveu(true);
        repo.save(i);
    }
}