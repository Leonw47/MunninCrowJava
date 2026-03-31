package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.*;
import br.com.munnincrow.api.repository.CampoPropostaRepository;
import br.com.munnincrow.api.repository.PropostaRepository;
import org.springframework.stereotype.Service;

@Service
public class IAPropostaService {

    private final PropostaRepository propostaRepo;
    private final CampoPropostaRepository campoRepo;

    public IAPropostaService(PropostaRepository propostaRepo,
                             CampoPropostaRepository campoRepo) {
        this.propostaRepo = propostaRepo;
        this.campoRepo = campoRepo;
    }

    public static String gerarTextoParaCampo(Long propostaId, String nomeCampo) {

        Proposta proposta = propostaRepo.findById(propostaId)
                .orElseThrow(() -> new IllegalArgumentException("Proposta não encontrada."));

        CampoProposta campo = campoRepo.findByPropostaAndNomeCampo(proposta, nomeCampo)
                .orElseThrow(() -> new IllegalArgumentException("Campo não encontrado."));

        Edital edital = proposta.getEdital();
        User usuario = proposta.getUsuario();

        // -------------------------------
        // MONTA O PROMPT PARA A IA
        // -------------------------------
        String prompt = """
                Você é um especialista em elaboração de propostas para editais públicos.
                Gere o texto para o campo "%s" da proposta.

                Dados do edital:
                - Título: %s
                - Objetivo: %s
                - Área temática: %s

                Dados do usuário:
                - Segmento: %s
                - Maturidade: %s
                - Faturamento anual: %s

                Campos já preenchidos:
                %s

                Gere um texto claro, objetivo e alinhado ao edital.
                """.formatted(
                nomeCampo,
                edital.getTitulo(),
                edital.getObjetivo(),
                edital.getAreaTematicaReal(),
                usuario.getSegmento(),
                usuario.getMaturidade(),
                usuario.getFaturamentoAnual(),
                proposta.getCampos().stream()
                        .map(c -> "- " + c.getNomeCampo() + ": " + c.getValor())
                        .reduce("", (a, b) -> a + "\n" + b)
        );

        // -------------------------------
        // CHAMADA PARA A IA (mock por enquanto)
        // -------------------------------
        String respostaIA = """
                [TEXTO GERADO AUTOMATICAMENTE]
                Este é um texto de exemplo para o campo "%s".
                """.formatted(nomeCampo);

        return respostaIA;
    }
}