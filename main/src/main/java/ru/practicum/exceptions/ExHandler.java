package ru.practicum.exceptions;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Builder
@RestControllerAdvice
public class ExHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("Not found: {}", e.toString());
        return ApiError.builder()
                .status(ErrorStatus.NOT_FOUND)
                .message(e.getMessage())
                .reason("Запрос несуществующего элемента")
                .build();

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictRequestException(final DataIntegrityViolationException e) {
        log.info("Conflict: {}", e.getMessage());
        return ApiError.builder()
                .status(ErrorStatus.CONFLICT)
                .message(Objects.requireNonNull(e.getMessage()).split(";")[0])
                .reason("В запросе невалидные значения для базы данных")
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final ValidationException e) {
        log.info("Bad request: {}", e.getMessage());
        return ApiError.builder()
                .status(ErrorStatus.BAD_REQUEST)
                .message(e.getMessage())
                .reason("Отправлены невалидные данные в запросе")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptionList(final MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .peek(e -> log.info("Validation error: {}", e.getDefaultMessage()))
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ApiError.builder()
                .errors(errors)
                .status(ErrorStatus.BAD_REQUEST)
                .message("Ошибка валидации")
                .reason("Отправлены невалидные данные в запросе")
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenRequestException(final ForbiddenException e) {
        log.info("Forbidden: {}", e.getMessage());
        return ApiError.builder()
                .status(ErrorStatus.FORBIDDEN)
                .message(e.getMessage())
                .reason("Отсутствуют права или доступ к объекту запрещен")
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleMethodNotAllowedException(final HttpRequestMethodNotSupportedException e) {
        log.info("Method not allowed: {}", e.getMessage());
        return ApiError.builder()
                .status(ErrorStatus.METHOD_NOT_ALLOWED)
                .message(e.getMessage())
                .reason("Метод, используемый в запросе, не поддерживается текущим эндпоинтом")
                .build();
    }
}
