package br.com.munnincrow.api.repository;

import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
    Optional<UserDeviceToken> findByUser(User user);
}