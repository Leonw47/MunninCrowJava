package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.UserNotificationPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNotificationPreferencesRepository extends JpaRepository<UserNotificationPreferences, Long> {
    Optional<UserNotificationPreferences> findByUsuario(User usuario);
}