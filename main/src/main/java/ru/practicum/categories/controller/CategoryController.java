package ru.practicum.categories.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Создана категория с id = {}", categoryDto.getId());
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Обновлена категория с id = {}", categoryDto.getId());
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        log.info("Удалена категория с id = {}", categoryId);
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto getCategory(@PathVariable Long categoryId) {
        log.info("Получена категория с id = {}", categoryId);
        return categoryService.getCategory(categoryId);
    }
}
