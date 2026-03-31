package br.com.munnincrow.api.spec;

import br.com.munnincrow.api.model.PropostaTutoria;
import br.com.munnincrow.api.model.enums.StatusPropostaTutoria;
import org.springframework.data.jpa.domain.Specification;

public class PropostaTutoriaSpec {

    public static Specification<PropostaTutoria> status(StatusPropostaTutoria status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<PropostaTutoria> autor(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("autor").get("id"), id);
    }

    public static Specification<PropostaTutoria> solicitacao(Long id) {
        return (root, query, cb) ->
                id == null ? null : cb.equal(root.get("solicitacao").get("id"), id);
    }
}