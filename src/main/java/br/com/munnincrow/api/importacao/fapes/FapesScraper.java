package br.com.munnincrow.api.importacao.fapes;

import br.com.munnincrow.api.importacao.ScraperEdital;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.enums.FonteImportacao;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FapesScraper implements ScraperEdital {

    private static final String BASE = "https://fapes.es.gov.br";

    private static final DateTimeFormatter BR_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<Edital> importar() {
        List<Edital> lista = new ArrayList<>();

        lista.addAll(importarPagina(
                BASE + "/inovacao",
                "Inovação",
                "Inovação"
        ));

        lista.addAll(importarPagina(
                BASE + "/difusao-do-conhecimento",
                "Difusão do Conhecimento",
                "Difusão do Conhecimento"
        ));

        lista.addAll(importarPagina(
                BASE + "/chamadas-internacionais",
                "Chamadas Internacionais",
                "Chamadas Internacionais"
        ));

        return lista;
    }

    private List<Edital> importarPagina(String url, String categoria, String areaTematica) {
        List<Edital> lista = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();

            Elements cards = doc.select(
                    ".card-edital, .edital, .item-edital, .card, article, .listagem-edital"
            );

            for (Element card : cards) {

                String titulo = extrairTexto(card, "h3, h2, .titulo, .card-title");
                String descricao = extrairTexto(card, "p, .descricao, .card-text");
                String link = extrairLink(card, "a");

                if (titulo == null || link == null) {
                    continue;
                }

                Edital edital = new Edital();
                edital.setTitulo(titulo);
                edital.setDescricaoCurta(descricao != null ? descricao : "");
                edital.setLinkOficial(link);
                edital.setOrgao(OrgaoEdital.FAPES);
                edital.setEstado("ES");
                edital.setCategoria(categoria);
                edital.setAreaTematica(areaTematica);
                edital.setFonte(FonteImportacao.SCRAPER);

                LocalDate[] datas = extrairDatasDoPdf(link);

                if (datas != null) {
                    edital.setDataAbertura(datas[0]);
                    edital.setDataEncerramento(datas[1]);
                } else {
                    edital.setDataAbertura(LocalDate.now().minusDays(5));
                    edital.setDataEncerramento(LocalDate.now().plusDays(30));
                }

                lista.add(edital);
            }

        } catch (Exception e) {
            System.err.println("Erro ao importar página da FAPES (" + url + "): " + e.getMessage());
        }

        return lista;
    }

    private String extrairTexto(Element card, String seletor) {
        Element el = card.selectFirst(seletor);
        return el != null ? el.text().trim() : null;
    }

    private String extrairLink(Element card, String seletor) {
        Element el = card.selectFirst(seletor);
        if (el == null) return null;

        String href = el.attr("href");
        if (href == null || href.isBlank()) return null;

        if (href.startsWith("/")) {
            return BASE + href;
        }

        return href;
    }

    private LocalDate[] extrairDatasDoPdf(String urlPdf) {
        try (PDDocument pdf = PDDocument.load(new URL(urlPdf).openStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(pdf);

            Pattern p = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");
            Matcher m = p.matcher(texto);

            List<LocalDate> datas = new ArrayList<>();

            while (m.find()) {
                String dataStr = m.group(1);
                try {
                    LocalDate data = LocalDate.parse(dataStr, BR_DATE);
                    datas.add(data);
                } catch (Exception ignored) {
                }
            }

            if (datas.size() >= 2) {
                return new LocalDate[]{datas.get(0), datas.get(datas.size() - 1)};
            }

        } catch (Exception e) {
            System.err.println("Erro ao extrair datas do PDF (" + urlPdf + "): " + e.getMessage());
        }

        return null;
    }
}