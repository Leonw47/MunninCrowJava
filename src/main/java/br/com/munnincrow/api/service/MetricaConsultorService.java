package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.MetricaConsultorResponse;
import br.com.munnincrow.api.model.AvaliacaoTutoria;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.AvaliacaoTutoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricaConsultorService {

    private final AvaliacaoTutoriaRepository avaliacaoRepo;
    private final UserService userService;

    public MetricaConsultorService(AvaliacaoTutoriaRepository avaliacaoRepo, UserService userService) {
        this.avaliacaoRepo = avaliacaoRepo;
        this.userService = userService;
    }

    public MetricaConsultorResponse calcular(Long consultorId) {

        User consultor = userService.buscarPorId(consultorId);
        if (consultor == null) throw new IllegalArgumentException("Consultor não encontrado.");

        List<AvaliacaoTutoria> avaliacoes = avaliacaoRepo.findByConsultorId(consultorId);

        MetricaConsultorResponse resp = new MetricaConsultorResponse();
        resp.consultorId = consultorId;
        resp.media = consultor.getMediaAvaliacoes();
        resp.totalAvaliacoes = consultor.getTotalAvaliacoes();

        resp.nota5 = (int) avaliacoes.stream().filter(a -> a.getNota() == 5).count();
        resp.nota4 = (int) avaliacoes.stream().filter(a -> a.getNota() == 4).count();
        resp.nota3 = (int) avaliacoes.stream().filter(a -> a.getNota() == 3).count();
        resp.nota2 = (int) avaliacoes.stream().filter(a -> a.getNota() == 2).count();
        resp.nota1 = (int) avaliacoes.stream().filter(a -> a.getNota() == 1).count();

        int positivas = resp.nota4 + resp.nota5;

        resp.percentualPositivas = resp.totalAvaliacoes > 0
                ? (positivas * 100.0) / resp.totalAvaliacoes
                : 0;

        return resp;
    }
}