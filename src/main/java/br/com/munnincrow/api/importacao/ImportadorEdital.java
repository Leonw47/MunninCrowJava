package br.com.munnincrow.api.importacao;

import br.com.munnincrow.api.dto.EditalImportadoDTO;
import java.util.List;

public interface ImportadorEdital {
    List<EditalImportadoDTO> importar();
}