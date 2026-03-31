package br.com.munnincrow.api.service;

import br.com.munnincrow.api.importacao.ScraperEdital;
import br.com.munnincrow.api.model.Edital;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EditalImportService {

    private final List<ScraperEdital> scrapers;

    public EditalImportService(List<ScraperEdital> scrapers) {
        this.scrapers = scrapers;
    }

    public List<Edital> importarTodos() {
        List<Edital> todos = new ArrayList<>();

        for (ScraperEdital scraper : scrapers) {
            try {
                todos.addAll(scraper.importar());
            } catch (Exception e) {
                // logar por scraper se quiser
            }
        }

        return todos;
    }
}