package br.com.suaempresa.apigerenciamento.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                erros.put(error.getField(), error.getDefaultMessage())
        );

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CupomInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleCupomInvalidoException(CupomInvalidoException ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());
        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CupomNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCupomNotFoundException(CupomNotFoundException ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());
        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());
        return new ResponseEntity<>(erros, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProdutoNotFoundException(ProdutoNotFoundException ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());
        return new ResponseEntity<>(erros, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidZeroOrNegativeException.class)
    public ResponseEntity<Map<String, String>> ValorMenorIgualAZeroException(InvalidZeroOrNegativeException ex) {
        Map<String, String> erros = new HashMap<>();
        erros.put("erro", ex.getMessage());
        return new ResponseEntity<>(erros, HttpStatus.NOT_FOUND);
    }


}
