package de.muenchen.dave.geodataeai.api.advice;

import de.muenchen.dave.geodataeai.api.dto.enums.InformationResponseType;
import de.muenchen.dave.geodataeai.api.dto.error.InformationResponseDto;
import de.muenchen.dave.geodataeai.domain.exception.GeometryOperationFailedException;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Zur Behandlung der in den Controller geworfenen Exceptions. Erforderlich um ein einheitliches
 * {@link InformationResponseDto} an den aufrufenden Dienst
 * zurückzugeben.
 * <p>
 * Die Methoden annotiert mit {@link Override} überschreiben alle handleXXX-Methoden des
 * {@link ResponseEntityExceptionHandler}s, um bei jeder Spring-Exception
 * einen einheitlichen Response-Body vom Typ {@link InformationResponseDto} zu gewährleisten.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GeometryOperationFailedException.class)
    public ResponseEntity<Object> handleGeometryOperationFailedException(final GeometryOperationFailedException ex) {
        final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage()));
        return ResponseEntity.status(httpStatus).body(errorResponseDto);
    }

    @ExceptionHandler(FeatureRequestFailedException.class)
    public ResponseEntity<Object> handleFeatureRequestFailedException(final FeatureRequestFailedException ex) {
        final var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage()));
        return ResponseEntity.status(httpStatus).body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
        final var httpStatus = HttpStatus.UNPROCESSABLE_CONTENT;
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage()));
        return ResponseEntity.status(httpStatus).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Die HTTP-Methode " + ex.getMethod() + " wird nicht unterstützt."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Content-Type " + ex.getContentType() + " wird nicht unterstützt."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            final HttpMediaTypeNotAcceptableException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Media-Type des Requests wird nicht unterstützt."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            final MissingPathVariableException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Pfadvariable " + ex.getVariableName() + " ist nicht gesetzt."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Requestparameter " + ex.getParameterName() + " ist nicht gesetzt."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            final ServletRequestBindingException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Fehler aufgetreten."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            final ConversionNotSupportedException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Fehler aufgetreten."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(
                List.of("Das Attribut " + ex.getPropertyName() + " besitzt nicht den korrekten Datentyp."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            final HttpMessageNotReadableException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(
                List.of("Die Nutzlast der Anfrage an das Backend konnte nicht verarbeitet werden."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            final HttpMessageNotWritableException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Nutzlast des Antwort vom Backend konnte nicht verarbeitet werden."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final Map<String, String> errors = new HashMap<>();
        ex
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    final FieldError fieldError = (FieldError) error;
                    final String fieldName = fieldError.getField();
                    final String errorMessage = fieldError.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        final List<String> errorMessages = errors
                .entrySet()
                .stream()
                .map(errorEntry -> "Attribut " + errorEntry.getKey() + ": " + StringUtils.capitalize(errorEntry.getValue()))
                .collect(Collectors.toList());

        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(errorMessages);
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     * <p>
     * Dieser Fehler tritt nur bei Multipart-Requests auf und muss bei dieser EAI nicht abgefangen
     * werden.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            final MissingServletRequestPartException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Es ist ein Fehler aufgetreten."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param request the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(
                List.of(
                        "Die URL " +
                                ex.getRequestURL() +
                                " konnte nicht mit der HTTP-Methode " +
                                ex.getHttpMethod() +
                                " aufgerufen werden."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler}, um ein einheitliches
     * {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex the exception
     * @param headers the headers to be written to the entity
     * @param status the selected entity status
     * @param webRequest the current geometry
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            final AsyncRequestTimeoutException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest webRequest) {
        super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Timeout aufgetreten."));
        return ResponseEntity.status(errorResponseDto.getHttpStatus()).headers(headers).body(errorResponseDto);
    }

    protected InformationResponseDto createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
            final Exception ex,
            final int httpStatus,
            final List<String> messages) {
        final var errorResponseDto = this.createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(httpStatus);
        errorResponseDto.setMessages(messages);
        return errorResponseDto;
    }

    protected InformationResponseDto createResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(final Exception ex) {
        final var errorResponseDto = new InformationResponseDto();
        errorResponseDto.setType(InformationResponseType.ERROR);
        errorResponseDto.setTimestamp(LocalDateTime.now());
        errorResponseDto.setOriginalException(ex.getClass().getSimpleName());
        return errorResponseDto;
    }
}
