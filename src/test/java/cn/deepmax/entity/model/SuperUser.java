package cn.deepmax.entity.model;



import cn.deepmax.entity.adapter.EnumType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "super_user" )
public class SuperUser extends User{

    @Column(name = "BIG_DECIMAL")
    private BigDecimal bigDecimal;

    private EnumType auth;



    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    @Column(name = "AUTH")
    @Enumerated(javax.persistence.EnumType.STRING)
    public EnumType getAuth() {
        return auth;
    }

    public void setAuth(EnumType auth) {
        this.auth = auth;
    }
}
