package cn.deepmax.entity.model;


import cn.deepmax.adapter.mapper.LocalDateTimeToTimestampMapper;
import cn.deepmax.adapter.mapper.LocalDateToDateMapper;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_NAME")
    private String userName;

    private LocalDateTime createTime;

    @Convert(converter = LocalDateToDateMapper.class)
    private LocalDate updateDate;

    @Column(name = "SHOW")
    private Boolean show;
    @Column(name = "HIDE")
    private boolean hide;

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
    @Column(name = "CREATE_TIME")

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    @Column(name = "UPDATE_DATE")
    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
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
