package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.HistoricoTutoriaResponse;
import br.com.munnincrow.api.service.HistoricoTutoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutorias")
public class HistoricoTutoriaController {

    private final HistoricoTutoriaService historicoService;

    public HistoricoTutoriaController(HistoricoTutoriaService historicoService) {
        this.historicoService = historicoService;
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoTutoriaResponse> listar(@PathVariable Long id) {
        return historicoService.listar(id);
    }
}