//${versionAndHashInfo}
package ${packageName};
import javax.persistence.*;

/**
* @desc	    generated by EasyQuery generator.
* @date 	${.now?string("yyyy/MM/dd")}
<#if isEntity ==true>
*           Entity object.
*/
@Entity
@Table(name = "${tableName}" ,catalog = "${catalogName}")
public class ${className} implements java.io.Serializable {
<#else >
*           Value Object.
*/
public class ${className} {
</#if>
    public ${className}(){}

    <#list columns as column>
    private ${column.propertyType} ${column.propertyName};
    </#list>

<#list columns as column>

    public void ${column.writeMethodName} (${column.propertyType} ${column.propertyName}){
        this.${column.propertyName} = ${column.propertyName} ;
    }
    <#if isEntity == true>
        <#if column.propertyName !="id">
    @Column(name = "${column.columnName}")
        <#else >
    @Id
    @Column(name = "id")
    @GeneratedValue
        </#if>
    </#if>
    public ${column.propertyType} ${column.readMethodName} (){
        return this.${column.propertyName} ;
    }
    <#else>

</#list>
}