package cn.deepmax.entity.model;

import cn.deepmax.annotation.Ignore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "super_user")
public class SuperUser extends User{

    @Column(name = "BIG_DECIMAL")
    private BigDecimal bigDecimal;
    @Ignore
    private String auth;

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
