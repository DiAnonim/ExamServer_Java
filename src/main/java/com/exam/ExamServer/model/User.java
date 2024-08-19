package com.exam.ExamServer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    // Уникальный идентификатор пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer user_id;

    // Имя пользователя, уникальное и обязательное поле
    @Column(name = "username", unique = true, nullable = false)
    @NotNull
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    // Электронная почта пользователя, уникальное и обязательное поле
    @Column(name = "email", unique = true, nullable = false)
    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    // Пароль пользователя, обязательное поле
    @Column(name = "password", nullable = false)
    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    // Количество дней хранения архивов для пользователя, по умолчанию 30 дней
    @Column(name = "archive_retention_days", columnDefinition = "integer default 30")
    private Integer archiveRetentionDays;
}
