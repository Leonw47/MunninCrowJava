package br.com.munnincrow.api.importacao;

import br.com.munnincrow.api.model.Edital;
import java.util.List;

public interface ScraperEdital {
    List<Edital> importar();
}