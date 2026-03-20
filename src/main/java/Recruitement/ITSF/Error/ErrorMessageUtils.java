package Recruitement.ITSF.Error;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Component
public class ErrorMessageUtils {

    private final MessageSource messageSource;

    public ErrorMessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public IErrorMessage buildErrorMessage(String code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        String label = messageSource.getMessage(code, args, code, locale);
        return new ErrorMessageImpl(code, label);
    }
}