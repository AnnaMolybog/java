package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorExceptionOnEvenSecond;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProcessorExceptionOnEvenSecondTest {

    @Test
    void processSuccessTest() throws Exception {
        var processor = new ProcessorExceptionOnEvenSecond(
            () -> LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 12, 31))
        );
        var message = new Message.Builder(1L).field8("field8").build();
        assertThat(processor.process(message)).isEqualTo(message);
    }

    @Test
    void processThrowExceptionTest() {
        var processor = new ProcessorExceptionOnEvenSecond(
            () -> LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 12, 30))
        );
        var message = new Message.Builder(1L).field8("field8").build();

        assertThatExceptionOfType(Exception.class).isThrownBy(() -> processor.process(message));
    }
}
