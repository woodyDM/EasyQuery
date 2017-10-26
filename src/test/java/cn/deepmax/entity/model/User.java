package cn.deepmax.entity.model;

import cn.deepmax.annotation.Ignore;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class User {
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    private Timestamp createTime;
    private Date updateDate;
    @Column(name = "OK1")
    private Boolean ok1;
    private boolean ok2;
    @Ignore
    private double point1;
    @Column(name = "POINT2")
    private Double point2;
    private Float point3;
    private float point4;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", ok1=").append(ok1);
        sb.append(", ok2=").append(ok2);
        sb.append(", point1=").append(point1);
        sb.append(", point2=").append(point2);
        sb.append(", point3=").append(point3);
        sb.append(", point4=").append(point4);
        sb.append('}');
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }



    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getOk1() {
        return ok1;
    }

    public void setOk1(Boolean ok1) {
        this.ok1 = ok1;
    }

    public boolean isOk2() {
        return ok2;
    }

    public void setOk2(boolean ok2) {
        this.ok2 = ok2;
    }

    public double getPoint1() {
        return point1;
    }

    public void setPoint1(double point1) {
        this.point1 = point1;
    }

    public Double getPoint2() {
        return point2;
    }

    public void setPoint2(Double point2) {
        this.point2 = point2;
    }

    public Float getPoint3() {
        return point3;
    }

    public void setPoint3(Float point3) {
        this.point3 = point3;
    }

    public float getPoint4() {
        return point4;
    }

    public void setPoint4(float point4) {
        this.point4 = point4;
    }
}
