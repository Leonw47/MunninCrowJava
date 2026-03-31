package br.com.munnincrow.api.importacao;

import br.com.munnincrow.api.dto.EditalImportadoDTO;
import br.com.munnincrow.api.util.DataEditalParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Component
public class ImportadorFAPES implements ImportadorEdital {

    private static final Map<String, String> URLS = Map.of(
            "Formação Científica", "https://fapes.es.gov.br/edital-aberto-forma%C3%A7%C3%A3o-cient%C3%ADfica",
            "Pesquisa", "https://fapes.es.gov.br/editais-abertos-pesquisa-4",
            "Difusão do Conhecimento", "https://fapes.es.gov.br/difusao-do-conhecimento",
            "Extensão", "https://fapes.es.gov.br/extensao",
            "Inovação", "https://fapes.es.gov.br/inovacao",
            "Chamadas Internacionais", "https://fapes.es.gov.br/chamadas-internacionais"
    );

    @Override
    public List<EditalImportadoDTO> importar() {
        List<EditalImportadoDTO> lista = new ArrayList<>();

        for (var entry : URLS.entrySet()) {
            String area = entry.getKey();
            String url = entry.getValue();

            try {
                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();

                Elements cards = doc.select(".panel");

                for (Element card : cards) {
                    EditalImportadoDTO dto = extrairEdital(card, area);
                    if (dto != null) {
                        lista.add(dto);
                    }
                }

            } catch (IOException e) {
                System.err.println("Erro ao acessar URL da FAPES: " + url);
                e.printStackTrace();
            }
        }

        return lista;
    }

    private EditalImportadoDTO extrairEdital(Element card, String area) {
        EditalImportadoDTO dto = new EditalImportadoDTO();

        Element tituloEl = card.selectFirst(".paneltitle-value");
        if (tituloEl == null) return null;

        dto.titulo = tituloEl.text().trim();

        Element linkEl = card.selectFirst("a[href]");
        if (linkEl == null) return null;

        dto.link = linkEl.absUrl("href").trim();
        if (dto.link.isBlank()) return null;

        Element datasEl = card.selectFirst("span:matches((?i)Período)");
        if (datasEl != null) {
            LocalDate[] datas = DataEditalParser.extrairDatas(datasEl.text());
            if (datas != null && datas.length == 2) {
                dto.dataAbertura = datas[0];
                dto.dataFechamento = datas[1];
            }
        }

        dto.categoria = area;
        dto.areaTematica = area;
        dto.orgao = "FAPES";
        dto.estado = "ES";

        return dto;
    }
}