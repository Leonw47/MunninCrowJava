package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.*;
import br.com.munnincrow.api.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PropostaService {

    private final PropostaRepository propostaRepo;
    private final FormularioEditalRepository formularioRepo;
    private final CampoFormularioRepository campoFormularioRepo;
    private final CampoPropostaRepository campoPropostaRepo;

    public PropostaService(PropostaRepository propostaRepo,
                           FormularioEditalRepository formularioRepo,
                           CampoFormularioRepository campoFormularioRepo,
                           CampoPropostaRepository campoPropostaRepo) {
        this.propostaRepo = propostaRepo;
        this.formularioRepo = formularioRepo;
        this.campoFormularioRepo = campoFormularioRepo;
        this.campoPropostaRepo = campoPropostaRepo;
    }

    public Proposta criarProposta(User usuario, Edital edital) {
        // Se já existir proposta para esse edital e usuário, retorna
        return propostaRepo.findByUsuarioAndEdital(usuario, edital)
                .orElseGet(() -> {
                    Proposta p = new Proposta();
                    p.setUsuario(usuario);
                    p.setEdital(edital);
                    p.setTitulo("Proposta para " + edital.getTitulo());
                    p.setStatus("rascunho");
                    p.setDataCriacao(LocalDate.now());
                    p.setDataAtualizacao(LocalDate.now());

                    Proposta salva = propostaRepo.save(p);

                    inicializarCamposDaProposta(salva);

                    return salva;
                });
    }

    public Proposta buscarPorId(Long id) {
        return propostaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));
    }

    public List<Proposta> listarPorUsuario(User usuario) {
        return propostaRepo.findByUsuario(usuario);
    }

    public List<Proposta> listarPorEdital(Edital edital) {
        return propostaRepo.findByEdital(edital);
    }

    public Proposta atualizarTituloEStatus(Long id, String titulo, String status) {
        Proposta p = buscarPorId(id);
        if (titulo != null && !titulo.isBlank()) {
            p.setTitulo(titulo);
        }
        if (status != null && !status.isBlank()) {
            p.setStatus(status);
        }
        p.setDataAtualizacao(LocalDate.now());
        return propostaRepo.save(p);
    }

    private void inicializarCamposDaProposta(Proposta proposta) {
        formularioRepo.findByEdital(proposta.getEdital())
                .ifPresent(formulario -> {
                    List<CampoFormulario> camposOficiais = campoFormularioRepo.findByFormulario(formulario);

                    for (CampoFormulario cf : camposOficiais) {
                        CampoProposta cp = new CampoProposta();
                        cp.setProposta(proposta);
                        cp.setNomeCampo(cf.getNomeCampo());
                        cp.setValor("");
                        cp.setConcluido(false);
                        campoPropostaRepo.save(cp);
                    }
                });
    }
}