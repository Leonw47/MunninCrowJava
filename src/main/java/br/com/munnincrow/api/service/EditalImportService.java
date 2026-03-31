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
        List<Edital> lista = new ArrayList<>();

        for (ScraperEdital scraper : scrapers) {
            try {
                lista.addAll(scraper.importar());
            } catch (Exception e) {
                System.err.println("Erro ao executar scraper "
                        + scraper.getClass().getSimpleName()
                        + ": " + e.getMessage());
            }
        }

        return lista;
    }
}