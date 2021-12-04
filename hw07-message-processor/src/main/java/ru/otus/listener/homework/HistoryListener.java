package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.History;
import ru.otus.model.Message;
import ru.otus.provider.DateTimeProvider;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {
    private final Deque<History> historyStack = new ArrayDeque<>();

    private final DateTimeProvider dateTimeProvider;

    public HistoryListener(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public void onUpdated(Message msg) throws CloneNotSupportedException {
        historyStack.push(new History(msg, dateTimeProvider.getDate()));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return historyStack.stream()
            .filter(historyEntry -> historyEntry.getMessage().getId() == id)
            .map(History::getMessage)
            .findAny();
    }
}
