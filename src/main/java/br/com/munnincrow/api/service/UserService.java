package br.com.munnincrow.api.service;

import br.com.munnincrow.api.model.User;
import br.com.munnincrow.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public List<User> listar() {
        return repository.findAll();
    }

    public User salvar(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        return repository.save(user);
    }

    public boolean deletar(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}