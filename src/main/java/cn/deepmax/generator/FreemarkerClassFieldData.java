package cn.deepmax.generator;


/**
 * define classInformation in Freemarker template.
 */
public class FreemarkerClassFieldData {
    private String propertyName;
    private String propertyType;        //property javaType
    private String writeMethodName;     //property
    private String readMethodName;
    private String columnName;          //property database columnName
    private String comment;

    public FreemarkerClassFieldData(String propertyName, String propertyType, String writeMethodName, String readMethodName, String columnName,String comment) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.writeMethodName = writeMethodName;
        this.readMethodName = readMethodName;
        this.columnName = columnName;
        this.comment = comment;

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getWriteMethodName() {
        return writeMethodName;
    }

    public void setWriteMethodName(String writeMethodName) {
        this.writeMethodName = writeMethodName;
    }

    public String getReadMethodName() {
        return readMethodName;
    }

    public void setReadMethodName(String readMethodName) {
        this.readMethodName = readMethodName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
