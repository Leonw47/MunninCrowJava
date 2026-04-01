package br.com.munnincrow.api.controller;

import br.com.munnincrow.api.dto.UserNotificationPreferencesDTO;
import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.model.UserNotificationPreferences;
import br.com.munnincrow.api.service.UserNotificationPreferencesService;
import br.com.munnincrow.api.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificacoes/preferencias")
public class UserNotificationPreferencesController {

    private final UserNotificationPreferencesService service;
    private final UserService usuarioService;

    public UserNotificationPreferencesController(
            UserNotificationPreferencesService service,
            UserService usuarioService
    ) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public UserNotificationPreferencesDTO buscarPreferencias() {
        User usuario = usuarioService.getUsuarioLogado();
        UserNotificationPreferences pref = service.buscar(usuario);

        UserNotificationPreferencesDTO dto = new UserNotificationPreferencesDTO();
        dto.receberEmail = pref.isReceberEmail();
        dto.receberPush = pref.isReceberPush();
        dto.apenasCriticos = pref.isApenasCriticos();
        dto.resumoDiario = pref.isResumoDiario();

        return dto;
    }

    @PutMapping
    public UserNotificationPreferencesDTO atualizar(@RequestBody UserNotificationPreferencesDTO dto) {
        User usuario = usuarioService.getUsuarioLogado();
        UserNotificationPreferences pref = service.atualizarPreferencias(usuario, dto);

        UserNotificationPreferencesDTO resp = new UserNotificationPreferencesDTO();
        resp.receberEmail = pref.isReceberEmail();
        resp.receberPush = pref.isReceberPush();
        resp.apenasCriticos = pref.isApenasCriticos();
        resp.resumoDiario = pref.isResumoDiario();

        return resp;
    }
}