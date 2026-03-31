package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.CampoFormulario;
import br.com.munnincrow.api.model.FormularioEdital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampoFormularioRepository extends JpaRepository<CampoFormulario, Long> {

    List<CampoFormulario> findByFormulario(FormularioEdital formulario);
}