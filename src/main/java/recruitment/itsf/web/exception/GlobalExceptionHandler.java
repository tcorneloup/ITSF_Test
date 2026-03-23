package recruitment.itsf.web.exception;

import recruitment.itsf.domain.exception.DomainException;
import recruitment.itsf.web.error.ErrorMessageUtils;
import recruitment.itsf.web.error.IErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final ErrorMessageUtils errorMessageUtils;

    public GlobalExceptionHandler(ErrorMessageUtils errorMessageUtils) {
        this.errorMessageUtils = errorMessageUtils;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<String> handleDomainException(DomainException ex) {
        log.error("Business rule violation: {}", ex.getErrorCode());
        IErrorMessage error = errorMessageUtils.buildErrorMessage(ex.getErrorCode());
        log.info("Error message created: code={}, label={}", error.getCode(), error.getLabel());
        return ResponseEntity.badRequest().body(error.getLabel());
    }
}
