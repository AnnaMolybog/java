package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.*;
import ru.otus.processor.homework.ProcessorExceptionOnEvenSecond;
import ru.otus.processor.homework.ProcessorSwapField11AndField12;
import ru.otus.provider.DateTimeProvider;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        DateTimeProvider dateTime = LocalDateTime::now;
        var processors = List.of(
            new ProcessorConcatFields(),
            new LoggerProcessor(new ProcessorUpperField10()),
            new LoggerProcessor(new ProcessorSwapField11AndField12()),
            new LoggerProcessor(new ProcessorExceptionOnEvenSecond(LocalDateTime::now))
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> {System.out.println(ex.getMessage());});
        var listenerPrinter = new ListenerPrinterConsole();
        var listenerHistory = new HistoryListener(dateTime);
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(listenerHistory);

        var message = new Message.Builder(1L)
            .field1("field1")
            .field2("field2")
            .field3("field3")
            .field6("field6")
            .field10("field10")
            .field11("field11")
            .field12("field12")
            .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(listenerHistory);
    }
}
