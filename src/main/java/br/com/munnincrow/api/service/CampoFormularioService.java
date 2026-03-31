package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.CampoFormulario;
import br.com.munnincrow.api.model.FormularioEdital;
import br.com.munnincrow.api.repository.CampoFormularioRepository;
import br.com.munnincrow.api.repository.FormularioEditalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampoFormularioService {

    private final CampoFormularioRepository campoRepo;
    private final FormularioEditalRepository formularioRepo;

    public CampoFormularioService(CampoFormularioRepository campoRepo,
                                  FormularioEditalRepository formularioRepo) {
        this.campoRepo = campoRepo;
        this.formularioRepo = formularioRepo;
    }

    public List<CampoFormulario> listarPorFormulario(Long formularioId) {
        FormularioEdital f = formularioRepo.findById(formularioId)
                .orElseThrow(() -> new IllegalArgumentException("Formulário não encontrado."));
        return campoRepo.findByFormulario(f);
    }

    public CampoFormulario salvar(CampoFormulario campo) {
        return campoRepo.save(campo);
    }

    public void deletar(Long id) {
        campoRepo.deleteById(id);
    }
}