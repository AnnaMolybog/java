package ru.otus.jdbc.mapper;

import ru.otus.jdbc.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public Field getIdField() {
        return this.getAllFields().stream()
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findFirst()
            .orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.getAllFields().stream()
            .filter(field -> field.getAnnotation(Id.class) == null)
            .collect(Collectors.toList());
    }
}
