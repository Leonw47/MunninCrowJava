package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.RankingConsultorResponse;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusSessao;
import br.com.munnincrow.api.repository.AvaliacaoTutoriaRepository;
import br.com.munnincrow.api.repository.PropostaTutoriaRepository;
import br.com.munnincrow.api.repository.SessaoTutoriaRepository;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingConsultorService {

    private final UserRepository userRepo;
    private final AvaliacaoTutoriaRepository avaliacaoRepo;
    private final SessaoTutoriaRepository sessaoRepo;
    private final PropostaTutoriaRepository propostaRepo;

    public RankingConsultorService(
            UserRepository userRepo,
            AvaliacaoTutoriaRepository avaliacaoRepo,
            SessaoTutoriaRepository sessaoRepo,
            PropostaTutoriaRepository propostaRepo
    ) {
        this.userRepo = userRepo;
        this.avaliacaoRepo = avaliacaoRepo;
        this.sessaoRepo = sessaoRepo;
        this.propostaRepo = propostaRepo;
    }

    public List<User> ranking() {
        return userRepo.findByTipoUsuarioOrderByMediaAvaliacoesDesc("CONSULTOR");
    }

    public RankingConsultorResponse buscarRanking(Long consultorId) {

        List<User> consultores = ranking();

        RankingConsultorResponse resp = new RankingConsultorResponse();
        resp.consultorId = consultorId;

        for (int i = 0; i < consultores.size(); i++) {
            if (consultores.get(i).getId().equals(consultorId)) {
                resp.posicao = i + 1;
                break;
            }
        }

        User consultor = userRepo.findById(consultorId).orElse(null);
        if (consultor == null) return resp;

        resp.mediaAvaliacoes = consultor.getMediaAvaliacoes();
        resp.totalAvaliacoes = consultor.getTotalAvaliacoes();

        resp.sessoesConcluidas = sessaoRepo.countByConsultorIdAndStatus(consultorId, StatusSessao.CONCLUIDA);
        resp.propostasAceitas = propostaRepo.countByAutorIdAndStatus(consultorId, StatusPropostaTutoria.ACEITA);

        resp.pontuacao =
                (resp.mediaAvaliacoes * 2) +
                        (resp.totalAvaliacoes * 0.5) +
                        (resp.sessoesConcluidas * 1.2) +
                        (resp.propostasAceitas * 1.5);

        resp.variacaoSemanal = 0;

        return resp;
    }
}