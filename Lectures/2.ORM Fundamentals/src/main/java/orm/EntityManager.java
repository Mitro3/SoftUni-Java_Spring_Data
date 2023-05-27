package orm;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static common.Constants.ExceptionMessages.*;
import static common.Constants.FormatMessages.*;
import static common.Constants.SQLQueries.*;

public class EntityManager<E> implements DBContext<E>{

    private final Connection connection;

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws SQLException, IllegalAccessException {
        Field primaryKey = getId(entity.getClass());
        primaryKey.setAccessible(true);

        Object value = primaryKey.get(entity);

        if (value == null || (long) value <= 0) {
            return doInsert(entity);
        }

        return doUpdate(entity, value);
    }

    @Override
    public Iterable<E> find(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return find(entityType, null);
    }

    @Override
    public Iterable<E> find(Class<E> entityType, String where) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String tableName = getTableName(entityType);

        String sql = String.format(SELECT_ALL_ENTITIES_BY_CRITERIA_QUERY,
                tableName, where == null ? "" : WHERE_KEY_WORD + where);

        ResultSet resultSet = this.connection.prepareStatement(sql).executeQuery();
        List<E> result = new ArrayList<>();

        E entity = this.createEntity(entityType, resultSet);

        while(entity != null) {
            result.add(entity);
            entity = this.createEntity(entityType, resultSet);
        }

        return result;
    }

    @Override
    public E findFirst(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(entityType, null);
    }

    @Override
    public E findFirst(Class<E> entityType, String where) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String tableName = getTableName(entityType);

        String sql = String.format(SELECT_ONE_ENTITY_BY_CRITERIA_QUERY,
                tableName, where == null ? "" : WHERE_KEY_WORD + where);

        ResultSet resultSet = this.connection.prepareStatement(sql).executeQuery();

        return this.createEntity(entityType, resultSet);
    }

    public void doDelete(E entity) throws SQLException, IllegalAccessException {
        String tableName = this.getTableName(entity.getClass());

        Field idField = getId(entity.getClass());

        String idName = getSQLColumnName(idField);
        Object idValue = getFieldValue(entity, idField);

        PreparedStatement deleteStatement = connection.
                prepareStatement(String.format(DELETE_ENTITY_QUERY, tableName, idName, idValue));
        deleteStatement.execute();
    }

    private Object getFieldValue(E entity, Field idName) throws IllegalAccessException {
        idName.setAccessible(true);

        return idName.get(entity);
    }

    public void doCreate(Class<E> entityType) throws SQLException {
        String tableName = this.getTableName(entityType);
        List<KeyValuePair> fieldsWithTypes = getFieldsNameAndTypeByKeyAndPair(entityType);

        String fieldsWithTypesFormatted = fieldsWithTypes.stream()
                .map(keyValuePair -> String.format(CREATE_VALUE_FORMAT, keyValuePair.key, keyValuePair.value))
                .collect(Collectors.joining(", "));

        PreparedStatement createStatement = connection
                .prepareStatement(String.format(CREATE_TABLE_QUERY, tableName, fieldsWithTypesFormatted));

        createStatement.execute();
    }

    public void doAlter(Class<E> entityType) throws SQLException {
        String tableName = this.getTableName(entityType);
        String addColumnsStatement = addColumnsStatementForNewFields(entityType, tableName);

        String alterQuery = String.format(ALTER_TABLE_FORMAT, tableName, addColumnsStatement);

        PreparedStatement statement = connection.prepareStatement(alterQuery);
        statement.execute();
    }

    private String addColumnsStatementForNewFields(Class<E> entityType, String tableName) throws SQLException {
        List<String> sqlColumns = getAllColumnNamesFromTable(entityType, tableName);
        List<Field> currFieldData = getAllFieldsDataWithoutId(entityType);

        List<String> newFieldsToBeInserted = new ArrayList<>();

        for (Field f : currFieldData) {
            String currFieldName = f.getName();

            if (sqlColumns.contains(currFieldName)) {
                continue;
            }

            String sqlTypeName = getSQLTypeName(f.getType());

            String addStatement = String.format(ADD_COLUMN_FORMAT, f.getName(), sqlTypeName);
            newFieldsToBeInserted.add(addStatement);
        }

        return String.join(", ", newFieldsToBeInserted);
    }

    private List<String> getAllColumnNamesFromTable(Class<E> entityType, String tableName) throws SQLException {
        List<String> allFields = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(GET_COLUMN_NAMES_BY_TABLE_NAME_QUERY);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            allFields.add(resultSet.getString(1));
        }

        return allFields;
    }

    private List<KeyValuePair> getFieldsNameAndTypeByKeyAndPair(Class<E> entityType) {
        return getAllFieldsDataWithoutId(entityType)
                .stream()
                .map(f -> new KeyValuePair(getSQLColumnName(f), getSQLTypeName(f.getType())))
                .collect(Collectors.toList());
    }

    private String getSQLColumnName(Field f) {
        if (f.getAnnotationsByType(Column.class).length != 0) {
            return f.getAnnotationsByType(Column.class)[0].name();
        }

        return f.getName();
    }

    private String getSQLTypeName(Class<?> type) {
        if (type == Integer.class || type == int.class) {
            return INT;
        } else if (type == String.class) {
            return VARCHAR;
        }

        return DATE;
    }

    private List<Field> getAllFieldsDataWithoutId(Class<E> entityType) {
        return Arrays.stream(entityType.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());
    }

    private boolean doInsert(E entity) throws IllegalAccessException, SQLException {
        String tableName = this.getTableName(entity.getClass());
        String fieldList = this.getAllFieldsNamesWithoutId(entity.getClass())
                .stream().map(f -> f.toString())
                .collect(Collectors.joining(", "));
        String valueList = this.getValuesWithoutId(entity);

        String insertQuery = String.format(INSERT_ENTITY_QUERY, tableName, fieldList, valueList);

        return this.connection.prepareStatement(insertQuery).execute();

    }

    private boolean doUpdate(E entity, Object primaryKey) throws SQLException, IllegalAccessException {
        String tableName = this.getTableName(entity.getClass());
        int id = Integer.parseInt(primaryKey.toString());

        List<KeyValuePair> keyValuePairs = getAllFieldsNameAndValueWithoutIdByKeyAndValuePairs(entity);

        String updatedValues = keyValuePairs.stream()
                .map(keyValuePair -> String.format(UPDATE_VALUE_FORMAT, keyValuePair.key, keyValuePair.value))
                .collect(Collectors.joining(", "));

        String updatedQuery = String.format(UPDATE_ENTITY_QUERY, tableName, updatedValues, id);

        return connection.prepareStatement(updatedQuery).execute();
    }

    private List<KeyValuePair> getAllFieldsNameAndValueWithoutIdByKeyAndValuePairs(E entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(Id.class) && f.isAnnotationPresent(Column.class))
                .map(f -> new KeyValuePair(f.getAnnotationsByType(Column.class)[0].name(),
                        mapFieldsToGivenType(f, entity)))
                .collect(Collectors.toList());
    }

    private String mapFieldsToGivenType(Field f, E entity) {
        f.setAccessible(true);

        Object o = null;

        try {
            o = f.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o instanceof String || o instanceof LocalDate
                ? "'" + o + "'"
                : Objects.requireNonNull(o).toString();
    }

    private E createEntity(Class<E> entityType, ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!resultSet.next()) {
            return null;
        }

        E entity = entityType.getDeclaredConstructor().newInstance();

        Field[] declaredFields = entityType.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(Column.class) && !declaredField.isAnnotationPresent(Id.class)) {
                continue;
            }

            Column columnAnnotation = declaredField.getAnnotation(Column.class);

            String fieldName = columnAnnotation == null ?
                    declaredField.getName() :
                    columnAnnotation.name();

            String value = resultSet.getString(fieldName);
            entity = this.fillData(entity, declaredField, value);
        }

        return entity;
    }

    private E fillData(E entity, Field field, String value) throws IllegalAccessException {
        field.setAccessible(true);

        if (field.getType() == long.class || field.getType() == Long.class) {
            field.setLong(entity, Long.parseLong(value));
        } else if (field.getType() == int.class || field.getType() == Integer.class){
            field.setInt(entity, Integer.parseInt(value));
        } else if (field.getType() == LocalDate.class) {
            field.set(entity, LocalDate.parse(value));
        } else if (field.getType() == String.class) {
            field.set(entity, value);
        } else {
            throw new ORMException("Unsupported type " + field.getType());
        }

        return entity;
    }

    private String getTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);

        if (annotation == null) {
            throw new ORMException(CLASS_MISSING_ENTITY_ANNOTATION);
        }

        return annotation.name();
    }

    private List<String> getAllFieldsNamesWithoutId(Class<?> entityType) {
       return Arrays.stream(entityType.getDeclaredFields())
                .filter(f -> f.getAnnotation(Column.class) != null)
                .map(f -> f.getAnnotation(Column.class).name())
                .collect(Collectors.toList());
    }

    private String getValuesWithoutId(E entity) throws IllegalAccessException {
        Field[] declaredFields = entity.getClass().getDeclaredFields();
        List<String> result = new ArrayList<>();

        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Column.class) == null) {
                continue;
            }

            declaredField.setAccessible(true);
            Object value = declaredField.get(entity);
            result.add("\"" + value.toString() + "\"");
        }

        return String.join(", ", result);
    }

    private Field getId(Class<?> entity) {
        return Arrays.stream(entity.getDeclaredFields())
                .filter(f -> f.getAnnotation(Id.class) != null)
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(ENTITY_MISSING_PRIMARY_KEY));
    }

    private record KeyValuePair(String key, String value) {};
}

