package com.exam.ExamServer.service;

import com.exam.ExamServer.model.User;
import com.exam.ExamServer.repository.IUserRepository;
import com.exam.ExamServer.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    // Репозиторий для работы с User
    private final IUserRepository userRepository;

    // Метод для поиска пользователя по ID.
    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    // Метод для поиска пользователя по имени пользователя (username).
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод для поиска пользователя по email.
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Метод для проверки существования пользователя по имени пользователя.
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Метод для проверки существования пользователя по email.
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Метод для создания нового пользователя. Если имя пользователя или email уже существует,
    // выбрасывается исключение IllegalArgumentException.
    @Override
    public User createUser(User user) {
        if (existsByUsername(user.getUsername()) || existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Username or email already exists.");
        }
        return userRepository.save(user);
    }

    // Метод для обновления данных пользователя. Если пользователь не существует,
    // выбрасывается исключение IllegalArgumentException.
    @Override
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getUser_id())) {
            throw new IllegalArgumentException("User does not exist.");
        }
        return userRepository.save(user);
    }

    // Метод для удаления пользователя по ID. Если пользователь не существует,
    // выбрасывается исключение IllegalArgumentException.
    @Override
    public void deleteUserById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exist.");
        }
        userRepository.deleteById(userId);
    }

    // Метод для аутентификации пользователя по имени пользователя и паролю.
    // Если пользователь найден и пароль совпадает, возвращается Optional с пользователем,
    // иначе возвращается пустой Optional.
    @Override
    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}
