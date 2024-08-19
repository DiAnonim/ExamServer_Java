package com.exam.ExamServer.repository;

import com.exam.ExamServer.model.Diary;
import com.exam.ExamServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IDiaryRepository extends JpaRepository<Diary, Integer> {

    // Поиск дневника по названию
    @Query("SELECT d FROM Diary d WHERE d.title = :title")
    Optional<Diary> findByTitle(@Param("title") String title);

    // Поиск всех дневников по автору
    @Query("SELECT d FROM Diary d WHERE d.author = :author")
    List<Diary> findByAuthor(@Param("author") User author);

    // Поиск всех дневников с определенным тегом
    @Query("SELECT d FROM Diary d WHERE d.tags LIKE %:tag%")
    List<Diary> findByTagsContaining(@Param("tag") String tag);

    // Поиск всех архивированных дневников
    @Query("SELECT d FROM Diary d WHERE d.isArchive = true")
    List<Diary> findByIsArchiveTrue();

    // Поиск всех неархивированных дневников
    @Query("SELECT d FROM Diary d WHERE d.isArchive = false")
    List<Diary> findByIsArchiveFalse();

    // Поиск всех дневников, созданные более 30 дней назад
    @Query("SELECT d FROM Diary d JOIN d.author a WHERE d.isArchive = true AND d.createdAt < :cutoffDate AND a.user_id = :userId")
    List<Diary> findExpiredArchives(@Param("cutoffDate") LocalDateTime cutoffDate, @Param("userId") Integer userId);

}
