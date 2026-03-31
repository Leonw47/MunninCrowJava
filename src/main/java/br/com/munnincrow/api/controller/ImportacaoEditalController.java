package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.service.EditalImportService;
import br.com.munnincrow.api.service.EditalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/importacao")
public class ImportacaoEditalController {

    private final EditalImportService importService;
    private final EditalService editalService;

    public ImportacaoEditalController(EditalImportService importService, EditalService editalService) {
        this.importService = importService;
        this.editalService = editalService;
    }

    @PostMapping("/executar")
    public ResponseEntity<String> executarImportacao() {
        List<Edital> editais = importService.importarTodos();
        int novos = 0;

        for (Edital edital : editais) {
            try {
                editalService.salvarImportado(edital);
                novos++;
            } catch (Exception ignored) {}
        }

        return ResponseEntity.ok("Importação concluída. Novos editais: " + novos);
    }
}