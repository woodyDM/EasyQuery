package cn.deepmax.model;


/**
 * column database information.
 */
public class ColumnMetaData {
    private String columnName;
    private String classTypeName;       //java type name, decided by jdbc?
    private String dbTypeName;      //database name
    private Integer precise;            //precise of data.
    private String comment;

    public ColumnMetaData(String columnName, String classTypeName, String dbTypeName, Integer precise,String comment) {
        this.columnName = columnName;
        this.classTypeName = classTypeName;
        this.dbTypeName = dbTypeName;
        this.precise = precise;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColumnMetaData that = (ColumnMetaData) o;

        if (!columnName.equals(that.columnName)) return false;
        if (!classTypeName.equals(that.classTypeName)) return false;
        if (!dbTypeName.equals(that.dbTypeName)) return false;
        if (precise != null ? !precise.equals(that.precise) : that.precise != null) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + classTypeName.hashCode();
        result = 31 * result + dbTypeName.hashCode();
        result = 31 * result + (precise != null ? precise.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
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

    public Integer getPrecise() {
        return precise;
    }

    public String getComment() {
        return comment;
    }

    public void setClassTypeName(String classTypeName) {
        this.classTypeName = classTypeName;
    }
}
