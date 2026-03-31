package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.model.DisponibilidadeConsultor;
import br.com.munnincrow.api.model.enums.DiaSemana;
import br.com.munnincrow.api.service.DisponibilidadeConsultorService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidade")
public class DisponibilidadeConsultorController {

    private final DisponibilidadeConsultorService service;

    public DisponibilidadeConsultorController(DisponibilidadeConsultorService service) {
        this.service = service;
    }

    @PostMapping("/{consultorId}")
    public DisponibilidadeConsultor criar(
            @PathVariable Long consultorId,
            @RequestParam DiaSemana dia,
            @RequestParam String inicio,
            @RequestParam String fim
    ) {
        return service.criar(
                consultorId,
                dia,
                LocalTime.parse(inicio),
                LocalTime.parse(fim)
        );
    }

    @GetMapping("/{consultorId}")
    public List<DisponibilidadeConsultor> listar(@PathVariable Long consultorId) {
        return service.listar(consultorId);
    }
}
