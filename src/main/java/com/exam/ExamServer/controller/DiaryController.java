package com.exam.ExamServer.controller;

import com.exam.ExamServer.model.Diary;
import com.exam.ExamServer.model.User;
import com.exam.ExamServer.service.interfaces.IDiaryService;
import com.exam.ExamServer.service.interfaces.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final IDiaryService diaryService;
    private final IUserService userService;

    // Получить дневник по идентификатору
    @GetMapping("/{diaryId}")
    public ResponseEntity<Diary> getDiaryById(@PathVariable Integer diaryId) {
        Optional<Diary> diary = diaryService.findById(diaryId);
        return diary.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Получить все дневники по автору
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Diary>> getDiariesByAuthor(@PathVariable Integer authorId) {
        Optional<User> author = userService.findById(authorId);
        if (author.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Diary> diaries = diaryService.findByAuthor(author.get());
        return ResponseEntity.ok(diaries);
    }

    // Найти дневники по тегу
    @GetMapping("/tags/{tag}")
    public ResponseEntity<List<Diary>> getDiariesByTag(@PathVariable String tag) {
        List<Diary> diaries = diaryService.findByTagsContaining(tag);
        return ResponseEntity.ok(diaries);
    }

    // Найти все архивированные дневники
    @GetMapping("/archive")
    public ResponseEntity<List<Diary>> getArchivedDiaries() {
        List<Diary> diaries = diaryService.findByIsArchiveTrue();
        return ResponseEntity.ok(diaries);
    }

    // Найти все неархивированные дневники
    @GetMapping("/active")
    public ResponseEntity<List<Diary>> getActiveDiaries() {
        List<Diary> diaries = diaryService.findByIsArchiveFalse();
        return ResponseEntity.ok(diaries);
    }

    // Создать новый дневник
    @PostMapping
    public ResponseEntity<Diary> createDiary(@RequestBody Diary diary, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> author = userService.findById(userId);
        if (author.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Дополнительная проверка, чтобы убедиться, что автор, переданный в запросе, совпадает с текущим пользователем
        if (!diary.getAuthor().getUser_id().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Diary createdDiary = diaryService.createDiary(diary);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiary);
    }

    // Обновить существующий дневник
    @PutMapping("/{diaryId}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Integer diaryId, @RequestBody Diary diary) {
        if (!diaryService.findById(diaryId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        diary.setDiary_id(diaryId);
        Diary updatedDiary = diaryService.updateDiary(diary);
        return ResponseEntity.ok(updatedDiary);
    }

    // Удалить дневник по идентификатору
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Integer diaryId) {
        if (!diaryService.findById(diaryId).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        diaryService.deleteDiaryById(diaryId);
        return ResponseEntity.noContent().build();
    }
}
