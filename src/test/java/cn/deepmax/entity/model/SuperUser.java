package cn.deepmax.entity.model;

import cn.deepmax.annotation.Ignore;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
@Table(name = "superuser" )
public class SuperUser extends User {

    @Column(name = "BIGDECIMAL")
    private BigDecimal bigDecimal;

    public SuperUser() {
    }
    @Ignore
    private String auth;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuperUser{");
        sb.append("bigDecimal=").append(bigDecimal);
        sb.append(", auth='").append(auth).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
