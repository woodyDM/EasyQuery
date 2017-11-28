package cn.deepmax.model;


/**
 * define classInformation in Freemarker template.
 */
public class ClassMetaData {
    private String propertyName;
    private String propertyType;        //property javaType
    private String writeMethodName;     //property
    private String readMethodName;
    private String columnName;          //property database columnName

    public ClassMetaData(String propertyName, String propertyType, String writeMethodName, String readMethodName, String columnName) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.writeMethodName = writeMethodName;
        this.readMethodName = readMethodName;
        this.columnName = columnName;
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
}
