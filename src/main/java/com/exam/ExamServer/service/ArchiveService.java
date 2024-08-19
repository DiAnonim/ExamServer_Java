package com.exam.ExamServer.service;

import com.exam.ExamServer.model.Diary;
import com.exam.ExamServer.model.User;
import com.exam.ExamServer.repository.IDiaryRepository;
import com.exam.ExamServer.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArchiveService {

    // Репозиторий для работы с Diary
    private final IDiaryRepository diaryRepository;

    // Репозиторий для работы с User
    private final IUserRepository userRepository;

    // Метод выполняется ежедневно в полночь и удаляет просроченные архивы.
    @Scheduled(cron = "0 0 0 * * ?") // Выполняется каждый день в полночь
    @Transactional // Используем транзакцию для сохранения изменений в базе данных
    public void removeExpiredArchives() {
        try {
            // Получаем всех пользователей
            List<User> users = userRepository.findAll();
            LocalDateTime now = LocalDateTime.now();

            // Проходим по каждому пользователю и удаляем просроченные архивы
            for (User user : users) {
                // Ищем дневники, которые нужно удалить
                List<Diary> expiredDiaries = diaryRepository.findExpiredArchives(now.minusDays(user.getArchiveRetentionDays()), user.getUser_id());

                // Удаляем найденные дневники
                for (Diary diary : expiredDiaries) {
                    diaryRepository.delete(diary);
                    log.info("Deleted expired diary with ID: {}", diary.getDiary_id());
                }
            }
        } catch (Exception e) {
            // Логируем ошибки возникшие во время удаления
            log.error("Error occurred while removing expired archives", e);
        }
    }

    // Метод для архивирования дневника по его идентификатору.
    // Если дневник найден, он переводится в архивное состояние.
    public void archiveDiary(Integer diaryId) {
        diaryRepository.findById(diaryId).ifPresent(diary -> {
            diary.setIsArchive(true);
            diaryRepository.save(diary);
            log.info("Diary with ID: {} has been archived", diaryId);
        });
    }

    // Метод для восстановления дневника из архива по его идентификатору.
    // Если дневник найден, он переводится в неархивное состояние.
    public void restoreDiary(Integer diaryId) {
        diaryRepository.findById(diaryId).ifPresent(diary -> {
            diary.setIsArchive(false);
            diaryRepository.save(diary);
            log.info("Diary with ID: {} has been restored", diaryId);
        });
    }
}
