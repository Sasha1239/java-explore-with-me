package ru.practicum.categories.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.utilits.PageableRequest;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    //Создание категории
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.save(CategoryMapper.fromCategoryDto(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    //Обновление категории
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = validationCategory(categoryDto.getId());
        category.setName(categoryDto.getName());

        categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(category);
    }

    //Удаление категории
    @Override
    public void deleteCategory(Long categoryId) {
        validationCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    //Получение всех категорий
    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Page<Category> categories = categoryRepository.findAll(PageableRequest.of(from, size));
        return CategoryMapper.toCategoryDtoPageList(categories);
    }

    //Получение категории
    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = validationCategory(categoryId);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category validationCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException("Неверный идентификатор категории"));
    }
}
