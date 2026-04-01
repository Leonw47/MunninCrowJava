package br.com.munnincrow.api.service;

import br.com.munnincrow.api.dto.UserNotificationPreferencesDTO;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.UserNotificationPreferences;
import br.com.munnincrow.api.repository.UserNotificationPreferencesRepository;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationPreferencesService {

    private final UserNotificationPreferencesRepository repo;

    public UserNotificationPreferencesService(UserNotificationPreferencesRepository repo) {
        this.repo = repo;
    }

    public UserNotificationPreferences getOrCreate(User usuario) {
        return repo.findByUsuario(usuario)
                .orElseGet(() -> {
                    UserNotificationPreferences pref = new UserNotificationPreferences();
                    pref.setUsuario(usuario);
                    return repo.save(pref);
                });
    }

    public UserNotificationPreferences atualizarPreferencias(User usuario, UserNotificationPreferencesDTO dto) {
        UserNotificationPreferences pref = getOrCreate(usuario);

        pref.setReceberEmail(dto.receberEmail);
        pref.setReceberPush(dto.receberPush);
        pref.setApenasCriticos(dto.apenasCriticos);
        pref.setResumoDiario(dto.resumoDiario);

        return repo.save(pref);
    }

    public UserNotificationPreferences buscar(User usuario) {
        return getOrCreate(usuario);
    }
}