package br.com.munnincrow.api.scheduler;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.service.EditalImportService;
import br.com.munnincrow.api.service.EditalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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
        logger.info("Iniciando importação automática de editais...");

        try {
            List<Edital> editais = importService.importarTodos();
            int novos = 0;
            int atualizados = 0;

            for (Edital edital : editais) {
                try {
                    Edital salvo = editalService.salvarImportado(edital);
                    if (salvo.getDataImportacao() != null) {
                        // aqui você pode diferenciar novo/atualizado se quiser,
                        // por enquanto vamos contar tudo como "processado"
                    }
                    novos++;
                } catch (Exception e) {
                    logger.warn("Edital ignorado (duplicado ou inválido): {} - Motivo: {}",
                            edital.getTitulo(), e.getMessage());
                }
            }

            logger.info("Importação concluída. Editais processados: {}", novos);

        } catch (Exception e) {
            logger.error("Erro durante importação automática de editais", e);
        }
    }
}