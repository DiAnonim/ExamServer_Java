package com.exam.ExamServer.service.interfaces;

import com.exam.ExamServer.model.User;

import java.util.Optional;

public interface IUserService {

    // Поиск пользователя по идентификатору
    Optional<User> findById(Integer userId);

    // Поиск пользователя по имени
    Optional<User> findByUsername(String username);

    // Поиск пользователя по email
    Optional<User> findByEmail(String email);

    // Проверка на существование пользователя с указанным именем пользователя
    boolean existsByUsername(String username);

    // ППроверка на существование пользователя с указанным email
    boolean existsByEmail(String email);

    // Создать нового пользователя
    User createUser(User user);

    // Обновить существующего пользователя
    User updateUser(User user);

    // Удалить пользователя по идентификатору
    void deleteUserById(Integer userId);

    // Аутентификация пользователя
    Optional<User> authenticate(String username, String password);
}
