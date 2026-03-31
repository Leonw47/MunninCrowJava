package br.com.munnincrow.api.service;

import br.com.munnincrow.api.importacao.ScraperEdital;
import br.com.munnincrow.api.model.Edital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EditalImportService {

    private static final Logger logger = LoggerFactory.getLogger(EditalImportService.class);

    private final List<ScraperEdital> scrapers;

    public EditalImportService(List<ScraperEdital> scrapers) {
        this.scrapers = scrapers;
    }

    public List<Edital> importarTodos() {
        logger.info("Iniciando execução dos scrapers de editais...");

        List<Edital> todos = new ArrayList<>();

        for (ScraperEdital scraper : scrapers) {
            String nome = scraper.getClass().getSimpleName();
            logger.info("Executando scraper: {}", nome);

            try {
                List<Edital> resultado = scraper.importar();
                logger.info("Scraper {} retornou {} editais.", nome, resultado.size());
                todos.addAll(resultado);

            } catch (Exception e) {
                logger.error("Erro no scraper {}: {}", nome, e.getMessage(), e);
            }
        }

        logger.info("Total de editais coletados de todos os scrapers: {}", todos.size());
        return todos;
    }
}