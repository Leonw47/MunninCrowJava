package br.com.munnincrow.api.scheduler;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.service.EditalImportService;
import br.com.munnincrow.api.service.EditalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EditalScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EditalScheduler.class);

    private final EditalImportService importService;
    private final EditalService editalService;

    public EditalScheduler(EditalImportService importService, EditalService editalService) {
        this.importService = importService;
        this.editalService = editalService;
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void importarEditaisAutomaticamente() {
        logger.info("=== Iniciando importação automática de editais ===");

        long inicio = System.currentTimeMillis();

        try {
            List<Edital> editais = importService.importarTodos();

            int novos = 0;
            int atualizados = 0;
            int erros = 0;

            for (Edital edital : editais) {
                try {
                    Optional<Edital> existente = editalService.buscarPorLinkOptional(edital.getLinkOficial());

                    editalService.salvarImportado(edital);

                    if (existente.isPresent()) {
                        atualizados++;
                    } else {
                        novos++;
                    }

                } catch (Exception e) {
                    erros++;
                    logger.warn("Falha ao salvar edital '{}': {}", edital.getTitulo(), e.getMessage());
                }
            }

            long duracao = System.currentTimeMillis() - inicio;

            logger.info("=== Importação concluída ===");
            logger.info("Novos: {}", novos);
            logger.info("Atualizados: {}", atualizados);
            logger.info("Erros: {}", erros);
            logger.info("Tempo total: {} ms", duracao);

        } catch (Exception e) {
            logger.error("Erro crítico durante importação automática", e);
        }
    }
}