package ec.sasf.ms_comp_prueba_freya_lopez.exception;

import ec.sasf.ms_comp_prueba_freya_lopez.service.ErrorLogService;
import ec.sasf.ms_comp_prueba_freya_lopez.web.DTO.ErrorResponse;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
 public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorLogService errorLogService;

    public GlobalExceptionHandler(ErrorLogService errorLogService) {
        this.errorLogService = errorLogService;
    }

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ErrorResponse> handleDataException(DataException e) {
        return error(BAD_REQUEST, e, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return error(BAD_REQUEST, e, e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException e) {
        return error(NOT_FOUND, e, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e, "Error inesperado");
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, Exception e, String mensaje) {
        logger.error("Exception: {}", mensaje, e);

        // Guardar en BD (ErrorLogEntity)
        errorLogService.registrarError(mensaje, e.toString(), null);

        ErrorResponse errorDetails = new ErrorResponse(
                LocalDateTime.now(),
                mensaje,
                e.getMessage()
        );

        return new ResponseEntity<>(errorDetails, status);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e, e.getMessage());
    }
    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ErrorResponse> handleUserBlocked(UserBlockedException e) {
        return error(HttpStatus.LOCKED, e, e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        return error(HttpStatus.UNAUTHORIZED, e, e.getMessage());
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException e) {
        return error(HttpStatus.BAD_REQUEST, e, e.getMessage());
    }


}
