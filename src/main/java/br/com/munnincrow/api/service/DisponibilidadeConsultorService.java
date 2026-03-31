package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.DisponibilidadeConsultor;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.enums.DiaSemana;
import br.com.munnincrow.api.repository.DisponibilidadeConsultorRepository;
import br.com.munnincrow.api.repository.IndisponibilidadeConsultorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DisponibilidadeConsultorService {

    private final DisponibilidadeConsultorRepository repo;
    private final IndisponibilidadeConsultorRepository indRepo;
    private final UserService userService;

    public DisponibilidadeConsultorService(
            DisponibilidadeConsultorRepository repo,
            IndisponibilidadeConsultorRepository indRepo,
            UserService userService
    ) {
        this.repo = repo;
        this.indRepo = indRepo;
        this.userService = userService;
    }

    public DisponibilidadeConsultor criar(Long consultorId, DiaSemana dia, LocalTime inicio, LocalTime fim) {
        User consultor = userService.buscarPorId(consultorId);

        DisponibilidadeConsultor d = new DisponibilidadeConsultor();
        d.setConsultor(consultor);
        d.setDia(dia);
        d.setInicio(inicio);
        d.setFim(fim);

        return repo.save(d);
    }

    public List<DisponibilidadeConsultor> listar(Long consultorId) {
        return repo.findByConsultorId(consultorId);
    }

    public boolean estaDisponivel(Long consultorId, LocalDateTime momento) {
        DiaSemana dia = DiaSemana.valueOf(momento.getDayOfWeek().name());

        boolean dentroHorario = repo.findByConsultorId(consultorId).stream()
                .anyMatch(d ->
                        d.getDia() == dia &&
                                !momento.toLocalTime().isBefore(d.getInicio()) &&
                                !momento.toLocalTime().isAfter(d.getFim())
                );

        boolean indisponivel = indRepo.findByConsultorId(consultorId).stream()
                .anyMatch(i ->
                        !momento.toLocalDate().isBefore(i.getInicio()) &&
                                !momento.toLocalDate().isAfter(i.getFim())
                );

        return dentroHorario && !indisponivel;
    }
}
