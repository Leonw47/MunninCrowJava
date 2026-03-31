package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.*;
import br.com.munnincrow.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExtracaoFormularioService {

    private final FormularioEditalRepository formularioRepo;
    private final CampoFormularioRepository campoRepo;

    public ExtracaoFormularioService(FormularioEditalRepository formularioRepo,
                                     CampoFormularioRepository campoRepo) {
        this.formularioRepo = formularioRepo;
        this.campoRepo = campoRepo;
    }

    public FormularioEdital extrairFormulario(Edital edital, MultipartFile arquivo) {

        // -----------------------------------------
        // 1. Envia PDF para IA (mock por enquanto)
        // -----------------------------------------
        List<CampoFormulario> camposExtraidos = extrairCamposViaIA(arquivo);

        // -----------------------------------------
        // 2. Cria ou atualiza o formulário
        // -----------------------------------------
        FormularioEdital formulario = formularioRepo.findByEdital(edital)
                .orElseGet(() -> {
                    FormularioEdital f = new FormularioEdital();
                    f.setEdital(edital);
                    return formularioRepo.save(f);
                });

        // Remove campos antigos
        campoRepo.deleteAll(campoRepo.findByFormulario(formulario));

        // Salva novos campos
        for (CampoFormulario cf : camposExtraidos) {
            cf.setFormulario(formulario);
            campoRepo.save(cf);
        }

        return formulario;
    }

    // -----------------------------------------
    // MOCK DA IA — substituiremos depois
    // -----------------------------------------
    private List<CampoFormulario> extrairCamposViaIA(MultipartFile arquivo) {

        List<CampoFormulario> campos = new ArrayList<>();

        CampoFormulario c1 = new CampoFormulario();
        c1.setNomeCampo("Resumo do Projeto");
        c1.setTipo("texto");
        c1.setObrigatorio(true);

        CampoFormulario c2 = new CampoFormulario();
        c2.setNomeCampo("Justificativa");
        c2.setTipo("texto");
        c2.setObrigatorio(true);

        CampoFormulario c3 = new CampoFormulario();
        c3.setNomeCampo("Orçamento Total");
        c3.setTipo("numero");
        c3.setObrigatorio(true);

        campos.add(c1);
        campos.add(c2);
        campos.add(c3);

        return campos;
    }
}