package cn.deepmax.easyquery.test.model;


import cn.deepmax.easyquery.test.adapter.EnumType;
import cn.deepmax.easyquery.test.adapter.JpaConverter;
import cn.deepmax.easyquery.test.adapter.MyColor;
import cn.deepmax.easyquery.test.adapter.MyConverter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "super_user" )
public class SuperUser extends User{

    @Column(name = "BIG_DECIMAL")
    private BigDecimal bigDecimal;

    @Column(name = "COLOR1")
    private MyColor color1; //nothing annotated


    @Column(name = "AUTH")
    @Enumerated(javax.persistence.EnumType.STRING)
    private EnumType auth;


    @Column(name = "COLOR2")
    @Enumerated(javax.persistence.EnumType.ORDINAL)
    private MyColor color2;

    @Column(name = "COLOR3")
    @Convert(converter = JpaConverter.class)
    private MyColor color3;
    @Transient      // transient
    @Convert(converter = MyConverter.class)
    @Column(name = "COLOR4")
    private MyColor color4;

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public MyColor getColor1() {
        return color1;
    }

    public void setColor1(MyColor color1) {
        this.color1 = color1;
    }

    public MyColor getColor2() {
        return color2;
    }

    public void setColor2(MyColor color2) {
        this.color2 = color2;
    }

    public MyColor getColor3() {
        return color3;
    }

    public void setColor3(MyColor color3) {
        this.color3 = color3;
    }

    public MyColor getColor4() {
        return color4;
    }

    public void setColor4(MyColor color4) {
        this.color4 = color4;
    }

    public EnumType getAuth() {
        return auth;
    }

    public void setAuth(EnumType auth) {
        this.auth = auth;
    }
}
