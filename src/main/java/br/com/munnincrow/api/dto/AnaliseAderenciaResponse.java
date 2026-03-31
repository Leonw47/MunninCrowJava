package br.com.munnincrow.api.dto;

import java.util.List;
import java.util.Map;

public class AnaliseAderenciaResponse {
    public double scoreGeral;
    public Map<String, Double> criterios;
    public List<SugestaoCampoResponse> sugestoes;
}

