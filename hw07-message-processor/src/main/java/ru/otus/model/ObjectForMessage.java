package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ObjectForMessage objectForMessage = (ObjectForMessage) super.clone();
        objectForMessage.setData(new ArrayList<>(data));
        return objectForMessage;
    }
}
