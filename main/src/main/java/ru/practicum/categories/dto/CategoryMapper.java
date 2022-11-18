package ru.practicum.categories.dto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import ru.practicum.categories.model.Category;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category fromCategoryDto(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public List<CategoryDto> toCategoryDtoPageList(Page<Category> categories) {
        return categories.stream().map(this::toCategoryDto).collect(Collectors.toList());
    }
}
