package br.com.munnincrow.api.importacao.fapes;

import br.com.munnincrow.api.importacao.ScraperEdital;
import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.enums.FonteImportacao;
import br.com.munnincrow.api.model.enums.OrgaoEdital;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FapesScraper implements ScraperEdital {

    private static final Logger logger = LoggerFactory.getLogger(FapesScraper.class);

    private static final String BASE = "https://fapes.es.gov.br";
    private static final DateTimeFormatter BR_NUMERIC = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final Map<String, Month> MESES = Map.ofEntries(
            Map.entry("janeiro", Month.JANUARY), Map.entry("jan", Month.JANUARY),
            Map.entry("fevereiro", Month.FEBRUARY), Map.entry("fev", Month.FEBRUARY),
            Map.entry("março", Month.MARCH), Map.entry("marco", Month.MARCH), Map.entry("mar", Month.MARCH),
            Map.entry("abril", Month.APRIL), Map.entry("abr", Month.APRIL),
            Map.entry("maio", Month.MAY),
            Map.entry("junho", Month.JUNE), Map.entry("jun", Month.JUNE),
            Map.entry("julho", Month.JULY), Map.entry("jul", Month.JULY),
            Map.entry("agosto", Month.AUGUST), Map.entry("ago", Month.AUGUST),
            Map.entry("setembro", Month.SEPTEMBER), Map.entry("set", Month.SEPTEMBER),
            Map.entry("outubro", Month.OCTOBER), Map.entry("out", Month.OCTOBER),
            Map.entry("novembro", Month.NOVEMBER), Map.entry("nov", Month.NOVEMBER),
            Map.entry("dezembro", Month.DECEMBER), Map.entry("dez", Month.DECEMBER)
    );

    @Override
    public List<Edital> importar() {
        logger.info("Iniciando scraper da FAPES...");

        List<Edital> lista = new ArrayList<>();

        lista.addAll(importarPagina(BASE + "/inovacao", "Inovação", "Inovação"));
        lista.addAll(importarPagina(BASE + "/difusao-do-conhecimento", "Difusão do Conhecimento", "Difusão do Conhecimento"));
        lista.addAll(importarPagina(BASE + "/chamadas-internacionais", "Chamadas Internacionais", "Chamadas Internacionais"));

        logger.info("Scraper FAPES finalizado. Total coletado: {}", lista.size());
        return lista;
    }

    private List<Edital> importarPagina(String url, String categoria, String areaTematica) {
        List<Edital> lista = new ArrayList<>();

        try {
            logger.info("Carregando página da FAPES: {}", url);
            Document doc = Jsoup.connect(url).timeout(15000).get();

            Elements cards = doc.select(".card-edital, .edital, .item-edital, .card, article, .listagem-edital");

            for (Element card : cards) {
                try {
                    String titulo = extrairTexto(card, "h3, h2, .titulo, .card-title");
                    String descricao = extrairTexto(card, "p, .descricao, .card-text");
                    String link = extrairLink(card, "a");

                    if (titulo == null || link == null) continue;

                    Edital edital = new Edital();
                    edital.setTitulo(titulo);
                    edital.setDescricaoCurta(descricao != null ? descricao : "");
                    edital.setLinkOficial(link);
                    edital.setOrgao(OrgaoEdital.FAPES);
                    edital.setEstado("ES");
                    edital.setCategoria(categoria);
                    edital.setAreaTematica(areaTematica);
                    edital.setFonte(FonteImportacao.SCRAPER);

                    Map<String, Object> dados = extrairDadosDoPdf(link);

                    edital.setDataAbertura((LocalDate) dados.getOrDefault("abertura", LocalDate.now().minusDays(5)));
                    edital.setDataEncerramento((LocalDate) dados.getOrDefault("encerramento", LocalDate.now().plusDays(30)));
                    edital.setValorMaximo((Double) dados.get("valor"));
                    edital.setObjetivo((String) dados.get("objetivo"));
                    edital.setPublicoAlvo((String) dados.get("publico"));

                    String areaReal = (String) dados.get("areaReal");
                    if (areaReal != null && !areaReal.isBlank()) {
                        edital.setAreaTematica(areaReal);
                    }

                    lista.add(edital);

                } catch (Exception e) {
                    logger.warn("Falha ao processar card de edital na página {}: {}", url, e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao carregar página da FAPES {}: {}", url, e.getMessage());
        }

        return lista;
    }

    private String extrairTexto(Element card, String seletor) {
        try {
            Element el = card.selectFirst(seletor);
            return el != null ? el.text().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extrairLink(Element card, String seletor) {
        try {
            Element el = card.selectFirst(seletor);
            if (el == null) return null;

            String href = el.attr("href");
            if (href == null || href.isBlank()) return null;

            if (href.startsWith("/")) return BASE + href;
            return href;

        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> extrairDadosDoPdf(String urlPdf) {
        Map<String, Object> dados = new HashMap<>();

        try (PDDocument pdf = PDDocument.load(new URL(urlPdf).openStream())) {

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(pdf);

            if (texto == null || texto.isBlank()) {
                logger.warn("PDF vazio ou ilegível: {}", urlPdf);
                return dados;
            }

            texto = texto.replaceAll("\\s+", " ").trim();

            List<String> linhas = Arrays.asList(texto.split("\\r?\\n"));
            List<String> relevantes = new ArrayList<>();

            for (String linha : linhas) {
                String l = linha.toLowerCase();

                if (l.contains("inscri") ||
                        l.contains("submiss") ||
                        l.contains("proposta") ||
                        l.contains("cronograma") ||
                        l.contains("período") ||
                        l.contains("prazo") ||
                        l.contains("encerr")) {

                    relevantes.add(linha);
                }
            }

            List<LocalDate> datas = new ArrayList<>();

            for (String linha : relevantes) {
                datas.addAll(extrairDatasNumericas(linha));
                datas.addAll(extrairDatasPorExtenso(linha));
            }

            if (datas.size() >= 2) {
                dados.put("abertura", datas.get(0));
                dados.put("encerramento", datas.get(datas.size() - 1));
            } else if (datas.size() == 1) {
                dados.put("abertura", datas.get(0).minusDays(30));
                dados.put("encerramento", datas.get(0));
            }

            dados.put("valor", extrairValorFinanceiro(texto));
            dados.put("areaReal", extrairAreaTematica(texto));
            dados.put("objetivo", extrairObjetivo(texto));
            dados.put("publico", extrairPublicoAlvo(texto));

        } catch (Exception e) {
            logger.error("Erro ao extrair dados do PDF {}: {}", urlPdf, e.getMessage());
        }

        return dados;
    }

    private List<LocalDate> extrairDatasNumericas(String linha) {
        List<LocalDate> datas = new ArrayList<>();

        Pattern p = Pattern.compile("(\\d{2}[./-]\\d{2}[./-]\\d{4})");
        Matcher m = p.matcher(linha);

        while (m.find()) {
            try {
                String raw = m.group(1).replace("-", "/").replace(".", "/");
                datas.add(LocalDate.parse(raw, BR_NUMERIC));
            } catch (Exception ignored) {}
        }

        return datas;
    }

    private List<LocalDate> extrairDatasPorExtenso(String linha) {
        List<LocalDate> datas = new ArrayList<>();

        Pattern p = Pattern.compile("(\\d{1,2})\\s*(de)?\\s*([a-zçãé]+)\\s*(de)?\\s*(\\d{4})",
                Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(linha.toLowerCase());

        while (m.find()) {
            try {
                int dia = Integer.parseInt(m.group(1));
                String mesStr = m.group(3).toLowerCase();
                int ano = Integer.parseInt(m.group(5));

                Month mes = MESES.get(mesStr);
                if (mes != null) {
                    datas.add(LocalDate.of(ano, mes, dia));
                }
            } catch (Exception ignored) {}
        }

        return datas;
    }

    private Double extrairValorFinanceiro(String texto) {
        try {
            Matcher m1 = Pattern.compile("R\\$\\s*([0-9\\.]+,[0-9]{2})").matcher(texto);
            if (m1.find()) {
                String raw = m1.group(1).replace(".", "").replace(",", ".");
                return Double.parseDouble(raw);
            }

            Matcher m2 = Pattern.compile("R\\$\\s*([0-9]+)\\s*(mil|milhão|milhões)").matcher(texto.toLowerCase());
            if (m2.find()) {
                double base = Double.parseDouble(m2.group(1));
                String unidade = m2.group(2);

                if (unidade.startsWith("milh")) return base * 1_000_000;
                if (unidade.startsWith("mil")) return base * 1000;
            }

        } catch (Exception ignored) {}

        return null;
    }

    private String extrairAreaTematica(String texto) {
        for (String linha : texto.split("\\r?\\n")) {
            String l = linha.toLowerCase();

            if (l.contains("área temática") ||
                    l.contains("linha de fomento") ||
                    l.contains("linha de apoio") ||
                    l.contains("área do conhecimento")) {

                int idx = linha.indexOf(":");
                if (idx != -1) return linha.substring(idx + 1).trim();
            }
        }
        return null;
    }

    private String extrairObjetivo(String texto) {
        Matcher m = Pattern.compile("objetivo[s]?:\\s*(.+?)(\\.|$)", Pattern.CASE_INSENSITIVE).matcher(texto);
        return m.find() ? m.group(1).trim() : null;
    }

    private String extrairPublicoAlvo(String texto) {
        Matcher m = Pattern.compile("público[- ]alvo:\\s*(.+?)(\\.|$)", Pattern.CASE_INSENSITIVE).matcher(texto);
        return m.find() ? m.group(1).trim() : null;
    }
}