package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapField11AndField12 implements Processor {
    @Override
    public Message process(Message message) throws CloneNotSupportedException {
        Message messageClone = (Message) message.clone();

        return message.toBuilder().field11(messageClone.getField12()).field12(messageClone.getField11()).build();
    }
}
