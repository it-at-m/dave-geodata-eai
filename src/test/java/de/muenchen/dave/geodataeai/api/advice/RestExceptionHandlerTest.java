package de.muenchen.dave.geodataeai.api.advice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.muenchen.dave.geodataeai.api.dto.error.InformationResponseDto;
import de.muenchen.dave.geodataeai.domain.exception.GeometryOperationFailedException;
import de.muenchen.dave.geodataeai.infrastructure.exception.FeatureRequestFailedException;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RestExceptionHandlerTest {

    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    public void beforeEach() {
        this.restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    void handleGeometryOperationFailedException() {
        final GeometryOperationFailedException geometryOperationFailedException = new GeometryOperationFailedException(
                "test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleGeometryOperationFailedException(geometryOperationFailedException);

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(responseDto.getMessages(), is(List.of("test")));
        assertThat(responseDto.getOriginalException(), is("GeometryOperationFailedException"));
    }

    @Test
    void handleFeatureRequestFailedException() {
        final FeatureRequestFailedException featureRequestFailedException = new FeatureRequestFailedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleFeatureRequestFailedException(featureRequestFailedException);

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(responseDto.getMessages(), is(List.of("test")));
        assertThat(responseDto.getOriginalException(), is("FeatureRequestFailedException"));
    }

    @Test
    void handleConstraintViolationException() {
        final ConstraintViolationException constraintViolationException = new ConstraintViolationException(
                "test",
                null);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleConstraintViolationException(constraintViolationException);

        assertThat(response.getStatusCode(), is(HttpStatus.UNPROCESSABLE_CONTENT));

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(responseDto.getMessages(), is(List.of("test")));
        assertThat(responseDto.getOriginalException(), is("ConstraintViolationException"));
    }
}
