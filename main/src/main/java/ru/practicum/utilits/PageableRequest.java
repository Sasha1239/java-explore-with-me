package ru.practicum.utilits;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

@AllArgsConstructor
public class PageableRequest implements Pageable {
    private final Integer from;
    private final Integer size;
    private final Sort sort;

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return from;
    }

    @Override
    @NonNull
    public Sort getSort() {
        return this.sort;
    }

    @Override
    @NonNull
    public Pageable next() {
        return new PageableRequest(this.getPageNumber() + 1, this.getPageSize(), this.getSort());
    }

    @Override
    @NonNull
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    @NonNull
    public Pageable first() {
        return this;
    }

    @Override
    @NonNull
    public Pageable withPage(int pageNumber) {
        return new PageableRequest(pageNumber, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}
