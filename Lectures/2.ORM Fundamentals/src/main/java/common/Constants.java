package common;

public class Constants {
    private Constants() {};

    public static class ExceptionMessages {
        public static final String CLASS_MISSING_ENTITY_ANNOTATION = "Provided class doesn't have Entity annotation";
        public static final String ENTITY_MISSING_PRIMARY_KEY = "Entity doesn't have primary key";
    }

    public static class SQLQueries {
        public static final String SELECT_ALL_ENTITIES_BY_CRITERIA_QUERY = "SELECT * FROM %s %s";
        public static final String SELECT_ONE_ENTITY_BY_CRITERIA_QUERY = "SELECT * FROM %s %s LIMIT 1";
        public static final String INSERT_ENTITY_QUERY = "INSERT INTO %s (%s) VALUES (%s)";
        public static final String UPDATE_ENTITY_QUERY = "UPDATE %s SET %s WHERE id = %d";
        public static final String DELETE_ENTITY_QUERY = "DELETE FROM %s WHERE %s = '%s'";
        public static final String GET_COLUMN_NAMES_BY_TABLE_NAME_QUERY = "SELECT `COLUMN_NAME` FROM `INFORMATION_SCHEMA`.`COLUMNS`" +
                " WHERE `TABLE_SCHEMA`='soft_uni' AND `COLUMN_NAME` != 'id' " +
                "AND `TABLE_NAME` = 'accounts'";
        public static final String CREATE_TABLE_QUERY =
                "CREATE TABLE %s (id INT PRIMARY KEY AUTO_INCREMENT, %s)";
    }

   public static class FormatMessages {
       public static final String ADD_COLUMN_FORMAT = "ADD COLUMN %s %s";
       public static final String ALTER_TABLE_FORMAT = "ALTER TABLE %s %s";
       public static final String UPDATE_VALUE_FORMAT = "%s = %s";
       public static final String CREATE_VALUE_FORMAT = "%s %s";
       public static final String WHERE_KEY_WORD = "WHERE ";
       public static final String INT = "INT";
       public static final String VARCHAR = "VARCHAR(45)";
       public static final String DATE = "DATE";
    }
}
