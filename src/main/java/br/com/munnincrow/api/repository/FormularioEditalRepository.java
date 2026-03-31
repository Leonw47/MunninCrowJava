package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.FormularioEdital;
import br.com.munnincrow.api.model.Edital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormularioEditalRepository extends JpaRepository<FormularioEdital, Long> {

    Optional<FormularioEdital> findByEdital(Edital edital);
}