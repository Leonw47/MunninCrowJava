package br.com.munnincrow.api.spec;

import br.com.munnincrow.api.model.SolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.StatusSolicitacaoTutoria;
import br.com.munnincrow.api.model.enums.TipoSolicitacao;
import org.springframework.data.jpa.domain.Specification;

public class SolicitacaoTutoriaSpec {

    public static Specification<SolicitacaoTutoria> status(StatusSolicitacaoTutoria status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<SolicitacaoTutoria> tipo(TipoSolicitacao tipo) {
        return (root, query, cb) ->
                tipo == null ? null : cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<SolicitacaoTutoria> empreendedor(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("empreendedor").get("id"), id);
    }

    public static Specification<SolicitacaoTutoria> consultor(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("consultor").get("id"), id);
    }

    public static Specification<SolicitacaoTutoria> texto(String termo) {
        return (root, query, cb) ->
                termo == null ? null : cb.like(cb.lower(root.get("descricao")), "%" + termo.toLowerCase() + "%");
    }
}