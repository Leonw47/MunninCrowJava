package br.com.munnincrow.api.dto;

import java.util.List;
import java.util.Map;

public class InsightsUsuarioResponse {

    public Map<String, Long> categoriasMaisAcessadas;
    public Map<String, Long> areasTematicasMaisAcessadas;
    public Map<String, Long> orgaosMaisAcessados;

    public long totalCliques;
    public long totalFavoritos;
    public long totalInscricoes;

    public List<InsightRecomendacao> motivosRecomendacao;

    public List<InsightOportunidade> oportunidadesNaoVistas;
}