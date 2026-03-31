package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.ConsultorResponse;
import br.com.munnincrow.api.dto.MetricaConsultorResponse;
import br.com.munnincrow.api.dto.RankingConsultorResponse;
import br.com.munnincrow.api.service.MetricaConsultorService;
import br.com.munnincrow.api.service.RankingConsultorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultores")
public class ConsultorRankingController {

    private final RankingConsultorService rankingService;
    private final MetricaConsultorService metricasService;

    public ConsultorRankingController(
            RankingConsultorService rankingService,
            MetricaConsultorService metricasService
    ) {
        this.rankingService = rankingService;
        this.metricasService = metricasService;
    }

    @GetMapping("/ranking")
    public List<ConsultorResponse> ranking() {
        return rankingService.ranking().stream()
                .map(ConsultorResponse::from)
                .toList();
    }

    @GetMapping("/{consultorId}/ranking")
    public RankingConsultorResponse rankingIndividual(@PathVariable Long consultorId) {
        return rankingService.buscarRanking(consultorId);
    }

    @GetMapping("/{consultorId}/metricas")
    public MetricaConsultorResponse metricas(@PathVariable Long consultorId) {
        return metricasService.calcular(consultorId);
    }
}