package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.Edital;
import br.com.munnincrow.api.model.InteracaoEdital;
import br.com.munnincrow.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteracaoEditalRepository extends JpaRepository<InteracaoEdital, Long> {

    List<InteracaoEdital> findByUsuario(User usuario);

    List<InteracaoEdital> findByUsuarioAndEdital(User usuario, Edital edital);
}