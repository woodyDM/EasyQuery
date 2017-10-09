package cn.deepmax.entity.entity;

import java.sql.Timestamp;

public class User {
    private Integer id1;
    private int id2;
    private Long id3;
    private long id4;
    private String name;
    private Timestamp createTime;
    private Boolean ok1;
    private boolean ok2;
    private double point1;
    private Double point2;
    private Float point3;
    private float point4;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id1=").append(id1);
        sb.append(", id2=").append(id2);
        sb.append(", id3=").append(id3);
        sb.append(", id4=").append(id4);
        sb.append(", name='").append(name).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", ok1=").append(ok1);
        sb.append(", ok2=").append(ok2);
        sb.append(", point1=").append(point1);
        sb.append(", point2=").append(point2);
        sb.append(", point3=").append(point3);
        sb.append(", point4=").append(point4);
        sb.append('}');
        return sb.toString();
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public Long getId3() {
        return id3;
    }

    public void setId3(Long id3) {
        this.id3 = id3;
    }

    public long getId4() {
        return id4;
    }

    public void setId4(long id4) {
        this.id4 = id4;
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
