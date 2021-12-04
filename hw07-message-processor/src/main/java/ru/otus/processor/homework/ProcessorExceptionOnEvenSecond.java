package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.provider.DateTimeProvider;

public class ProcessorExceptionOnEvenSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionOnEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) throws Exception {
        if (this.dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new Exception("Throw exception on even second - done!");
        }
        return message;
    }
}
