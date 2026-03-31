package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.CampoProposta;
import br.com.munnincrow.api.model.Proposta;
import br.com.munnincrow.api.repository.CampoPropostaRepository;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CampoPropostaService {

    private final CampoPropostaRepository campoRepo;
    private final PropostaRepository propostaRepo;

    public CampoPropostaService(CampoPropostaRepository campoRepo,
                                PropostaRepository propostaRepo) {
        this.campoRepo = campoRepo;
        this.propostaRepo = propostaRepo;
    }

    public List<CampoProposta> listarPorProposta(Long propostaId) {
        Proposta p = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
        return campoRepo.findByProposta(p);
    }

    public CampoProposta salvarValorCampo(Long propostaId, String nomeCampo, String valor) {
        Proposta p = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        CampoProposta campo = campoRepo.findByPropostaAndNomeCampo(p, nomeCampo)
                .orElseGet(() -> {
                    CampoProposta novo = new CampoProposta();
                    novo.setProposta(p);
                    novo.setNomeCampo(nomeCampo);
                    novo.setConcluido(false);
                    return novo;
                });

        if (!campo.isConcluido()) {
            campo.setValor(valor);
            campoRepo.save(campo);

            p.setDataAtualizacao(LocalDate.now());
            propostaRepo.save(p);
        }

        return campo;
    }

    public CampoProposta marcarConcluido(Long campoId, boolean concluido) {
        CampoProposta campo = campoRepo.findById(campoId)
                .orElseThrow(() -> new IllegalArgumentException("Campo da proposta não encontrado."));

        campo.setConcluido(concluido);
        return campoRepo.save(campo);
    }

    public CampoProposta salvarValorGerado(Long propostaId, String nomeCampo, String textoGerado) {

        Proposta p = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        CampoProposta campo = campoRepo.findByPropostaAndNomeCampo(p, nomeCampo)
                .orElseThrow(() -> new IllegalArgumentException("Campo não encontrado."));

        if (!campo.isConcluido()) {
            campo.setValor(textoGerado);
            campoRepo.save(campo);

            p.setDataAtualizacao(LocalDate.now());
            propostaRepo.save(p);
        }

        return campo;
    }
}