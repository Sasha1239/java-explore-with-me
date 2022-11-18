package ru.practicum.utilits;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.exceptions.ValidationException;

public class PageableRequest extends PageRequest {
    private final long offset;

    private PageableRequest(int from, int size, Sort sort) {
        super(from / size, size, sort);
        offset = from;
    }

    public static PageableRequest of(int from, int size) {
        validationPage(from, size);
        return new PageableRequest(from, size, Sort.unsorted());
    }

    public static PageableRequest of(int from, int size, Sort sort) {
        validationPage(from, size);
        return new PageableRequest(from, size, sort);
    }

    private static void validationPage(int from, int size) {
        if (from < 0 || size < 1) {
            throw new ValidationException("No positive values in the pagination");
        }
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
