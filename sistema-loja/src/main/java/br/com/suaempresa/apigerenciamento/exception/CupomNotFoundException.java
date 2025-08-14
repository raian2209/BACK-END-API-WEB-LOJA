package br.com.suaempresa.apigerenciamento.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CupomNotFoundException extends RuntimeException {
    public CupomNotFoundException(String message) { super(message); }
}