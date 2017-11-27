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
    @Column(name = "USER_NAME")
    private String userName;
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;

    private Date updateDate;

    @Column(name = "SHOW")
    private Boolean show;
    @Column(name = "HIDE")
    private boolean hide;
    @Ignore
    private double transientProperty;
    @Column(name = "A_BIG_POINT")
    private Double aBigPoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public double getTransientProperty() {
        return transientProperty;
    }

    public void setTransientProperty(double transientProperty) {
        this.transientProperty = transientProperty;
    }

    public Double getaBigPoint() {
        return aBigPoint;
    }

    public void setaBigPoint(Double aBigPoint) {
        this.aBigPoint = aBigPoint;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", userName='").append(userName).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", isShow=").append(show);
        sb.append(", hide=").append(hide);
        sb.append(", transientProperty=").append(transientProperty);
        sb.append(", aBigPoint=").append(aBigPoint);
        sb.append('}');
        return sb.toString();
    }
}
