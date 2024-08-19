package com.exam.ExamServer.service;

import com.exam.ExamServer.model.Diary;
import com.exam.ExamServer.model.User;
import com.exam.ExamServer.repository.IDiaryRepository;
import com.exam.ExamServer.service.interfaces.IDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryService implements IDiaryService {

    // Репозиторий для работы с Diary
    private final IDiaryRepository diaryRepository;

    // Метод для поиска дневника по ID
    @Override
    public Optional<Diary> findById(Integer diaryId) {
        return diaryRepository.findById(diaryId);
    }

    // Метод для поиска дневника по его названию
    @Override
    public Optional<Diary> findByTitle(String title) {
        return diaryRepository.findByTitle(title);
    }

    // Метод для поиска дневников по автору
    @Override
    public List<Diary> findByAuthor(User author) {
        return diaryRepository.findByAuthor(author);
    }

    // Метод для поиска дневников по тегу
    @Override
    public List<Diary> findByTagsContaining(String tag) {
        return diaryRepository.findByTagsContaining(tag);
    }

    // Метод для поиска архивированных дневников
    @Override
    public List<Diary> findByIsArchiveTrue() {
        return diaryRepository.findByIsArchiveTrue();
    }

    // Метод для поиска неархивированных дневников
    @Override
    public List<Diary> findByIsArchiveFalse() {
        return diaryRepository.findByIsArchiveFalse();
    }

    // Метод для создания нового дневника
    @Override
    public Diary createDiary(Diary diary) {
        return diaryRepository.save(diary);
    }

    // Метод для обновления существующего дневника
    @Override
    public Diary updateDiary(Diary diary) {
        if (!diaryRepository.existsById(diary.getDiary_id())) {
            throw new IllegalArgumentException("Diary does not exist.");
        }
        return diaryRepository.save(diary);
    }

    // Метод для архивирования дневника по его идентификатору
    @Override
    public void archiveDiary(Integer diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Diary does not exist."));
        diary.setIsArchive(true);
        diaryRepository.save(diary);
    }

    // Метод для восстановления дневника из архива по его идентификатору
    @Override
    public void restoreDiary(Integer diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("Diary does not exist."));
        diary.setIsArchive(false);
        diaryRepository.save(diary);
    }

    // Метод для удаления дневника по его идентификатору
    @Override
    public void deleteDiaryById(Integer diaryId) {
        if (!diaryRepository.existsById(diaryId)) {
            throw new IllegalArgumentException("Diary does not exist.");
        }
        diaryRepository.deleteById(diaryId);
    }

    // Метод для поиска архивированных дневников, срок хранения которых истек
    public List<Diary> findExpiredArchives(LocalDateTime now, Integer days, Integer userId) {
        LocalDateTime cutoffDate = now.minus(days, ChronoUnit.DAYS);
        return diaryRepository.findExpiredArchives(cutoffDate, userId);
    }
}
