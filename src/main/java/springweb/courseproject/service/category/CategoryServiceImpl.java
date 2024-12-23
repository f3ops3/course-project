package springweb.courseproject.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springweb.courseproject.dto.category.CategoryResponseDto;
import springweb.courseproject.dto.category.CreateCategoryRequestDto;
import springweb.courseproject.exception.EntityNotFoundException;
import springweb.courseproject.mapper.CategoryMapper;
import springweb.courseproject.model.Category;
import springweb.courseproject.repository.category.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find category with id: " + id))
        );
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDto)));
    }

    @Override
    public CategoryResponseDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Couldn't find category with id: " + id)
        );
        categoryMapper.updateCategory(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
