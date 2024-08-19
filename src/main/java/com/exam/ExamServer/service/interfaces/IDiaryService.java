package com.exam.ExamServer.service.interfaces;

import com.exam.ExamServer.model.Diary;
import com.exam.ExamServer.model.User;

import java.util.List;
import java.util.Optional;

public interface IDiaryService {
    // Поиск дневника по идентификатору
    Optional<Diary> findById(Integer diaryId);

    // Поиск дневника по названию
    Optional<Diary> findByTitle(String title);

    // Поиск всех дневников по автору
    List<Diary> findByAuthor(User author);

    // Поиск всех дневников с определенным тегом
    List<Diary> findByTagsContaining(String tag);

    // Поиск всех архивированных дневников
    List<Diary> findByIsArchiveTrue();

    // Поиск всех неархивированных дневников
    List<Diary> findByIsArchiveFalse();

    // Создать новый дневник
    Diary createDiary(Diary diary);

    // Обновить существующий дневник
    Diary updateDiary(Diary diary);

    // Перенести дневник в архив
    void archiveDiary(Integer diaryId);

    // Восстановить дневник из архива
    void restoreDiary(Integer diaryId);

    // Удалить дневник по идентификатору
    void deleteDiaryById(Integer diaryId);
}
