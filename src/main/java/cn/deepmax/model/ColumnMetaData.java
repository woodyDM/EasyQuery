package cn.deepmax.model;


/**
 * column database information.
 */
public class ColumnMetaData {
    private String columnName;
    private String classTypeName;       //java type name, decided by jdbc?
    private String dbTypeName;      //database name
    private int precise;            //precise of data.

    public ColumnMetaData(String columnName, String classTypeName, String dbTypeName, int precise) {
        this.columnName = columnName;
        this.classTypeName = classTypeName;
        this.dbTypeName = dbTypeName;
        this.precise = precise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnMetaData that = (ColumnMetaData) o;

        if (precise != that.precise) return false;
        if (!columnName.equals(that.columnName)) return false;
        if (!classTypeName.equals(that.classTypeName)) return false;
        return dbTypeName.equals(that.dbTypeName);
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + classTypeName.hashCode();
        result = 31 * result + dbTypeName.hashCode();
        result = 31 * result + precise;
        return result;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getClassTypeName() {
        return classTypeName;
    }

    public String getDbTypeName() {
        return dbTypeName;
    }

    public int getPrecise() {
        return precise;
    }
}
