package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.*;
import br.com.munnincrow.api.repository.CampoFormularioRepository;
import br.com.munnincrow.api.repository.FormularioEditalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormularioEditalService {

    private final FormularioEditalRepository formularioRepo;
    private final CampoFormularioRepository campoRepo;

    public FormularioEditalService(FormularioEditalRepository formularioRepo,
                                   CampoFormularioRepository campoRepo) {
        this.formularioRepo = formularioRepo;
        this.campoRepo = campoRepo;
    }

    public FormularioEdital buscarPorEdital(Edital edital) {
        return formularioRepo.findByEdital(edital)
                .orElseThrow(() -> new IllegalArgumentException("Formulário do edital não encontrado."));
    }

    public FormularioEdital salvarOuAtualizar(Edital edital, List<CampoFormulario> camposExtraidos) {
        FormularioEdital formulario = formularioRepo.findByEdital(edital)
                .orElseGet(() -> {
                    FormularioEdital f = new FormularioEdital();
                    f.setEdital(edital);
                    return formularioRepo.save(f);
                });

        // Remove campos antigos
        List<CampoFormulario> antigos = campoRepo.findByFormulario(formulario);
        campoRepo.deleteAll(antigos);

        // Adiciona novos campos
        for (CampoFormulario cf : camposExtraidos) {
            cf.setFormulario(formulario);
            campoRepo.save(cf);
        }

        return formulario;
    }

    public List<CampoFormulario> listarCampos(Edital edital) {
        FormularioEdital formulario = buscarPorEdital(edital);
        return campoRepo.findByFormulario(formulario);
    }
}