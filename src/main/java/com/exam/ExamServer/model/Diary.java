package com.exam.ExamServer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diary")
public class Diary {

    // Уникальный идентификатор дневника
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Integer diary_id;

    // Ссылка на фото, по умолчанию установлена ссылка на изображение
    @Column(name = "photo_link", columnDefinition = "text default 'https://1.bp.blogspot.com/-BXV4NsmA43Y/WLf4BZwJiOI/AAAAAAAATU8/rEKAYq7bb8c1BjXagUD3FpHN_yX53X36QCLcB/s1600/thumbnail.gif'")
    private String photoLink;

    // Название дневника, обязательное поле
    @Column(name = "title",  nullable = false)
    @NotNull
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    // Содержание дневника
    @Column(name = "content")
    private String content;

    // Автор дневника, обязательное поле, связанное с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull
    @NotEmpty(message = "Author cannot be empty")
    private User author;

    // Теги дневника, сохраненные в отдельной таблице
    @ElementCollection
    @CollectionTable(name = "diary_tags", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "tag")
    private Set<String> tags;

    // Список соавторов дневника, связанный через промежуточную таблицу
    @ManyToMany
    @JoinTable(
            name = "diary_collaborators",
            joinColumns = @JoinColumn(name = "diary_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> collaborators;;

    // Флаг, указывающий, архивирован ли дневник
    @Column(name = "is_archive")
    private Boolean isArchive;

    // Дата создания дневника
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Дата последнего обновления дневника
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Метод, автоматически устанавливающий дату создания перед сохранением в базу данных
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Метод, автоматически обновляющий дату последнего изменения перед обновлением в базе данных
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
