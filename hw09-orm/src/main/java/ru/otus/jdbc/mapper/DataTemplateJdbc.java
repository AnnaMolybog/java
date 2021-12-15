package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
        DbExecutor dbExecutor,
        EntitySQLMetaData entitySQLMetaData,
        EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return this.dbExecutor.executeSelect(connection, this.entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return this.convertResultSetToObject(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, this.entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var objectsList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    objectsList.add(this.convertResultSetToObject(rs));
                }
                return objectsList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            var fieldsValues = this.getFieldsValues(client);
            return dbExecutor.executeStatement(connection, this.entitySQLMetaData.getInsertSql(), fieldsValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            var fieldsValues = this.getFieldsValues(client);
            var idField = this.entityClassMetaData.getIdField();
            idField.setAccessible(true);
            fieldsValues.add(idField.get(client));
            dbExecutor.executeStatement(connection, this.entitySQLMetaData.getUpdateSql(), fieldsValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private T convertResultSetToObject(ResultSet rs) throws SQLException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var entity = this.entityClassMetaData.getConstructor().newInstance();
        for (Field field : this.entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            field.set(entity, rs.getObject(field.getName()));
        }

        return entity;
    }

    private List<Object> getFieldsValues(T client) {
        return this.entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            try {
                field.setAccessible(true);
                return field.get(client);
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }).collect(Collectors.toList());
    }
}
