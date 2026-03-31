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
public class ImportadorFAPERJ implements ImportadorEdital {

    private static final String URL = "https://www.faperj.br/?id=10.4.2";

    @Override
    public List<EditalImportadoDTO> importar() {
        List<EditalImportadoDTO> lista = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0")
                    .timeout(15000)
                    .get();

            // Estruturas comuns da FAPERJ: .chamada, .card, <li>
            Elements cards = doc.select(".chamada, .card, li");

            for (Element card : cards) {
                Element linkEl = card.selectFirst("a[href]");
                if (linkEl == null) continue;

                EditalImportadoDTO dto = new EditalImportadoDTO();
                dto.titulo = linkEl.text().trim();
                dto.link = linkEl.absUrl("href");

                // Datas: "Inscrições até DD/MM/AAAA" ou "Data limite: DD/MM/AAAA"
                Element dataEl = card.selectFirst("p:matches((?i)Inscrições|Data limite)");
                if (dataEl != null) {
                    LocalDate[] datas = DataEditalParser.extrairDatas(dataEl.text());
                    dto.dataAbertura = datas[0];
                    dto.dataFechamento = datas[1];
                }

                dto.orgao = "FAPERJ";
                dto.estado = "RJ";
                dto.categoria = "Pesquisa";
                dto.areaTematica = "Pesquisa";

                lista.add(dto);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }
}