package cn.deepmax.easyquery.entity.model;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "student", schema = "public")
public class Student {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "room_id")
    private Long roomId;

    @Column(name ="my_FLOAT")
    private Float aFloat;

    @Column(name = "my_show")
    private Boolean show;


    @Column(name = "my_decimal")
    private BigDecimal decimal;


    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
