package cn.deepmax.entity.model;

import java.math.BigDecimal;

public class SuperUser extends User {

    private BigDecimal bigDecimal;

    public SuperUser() {
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
        sb.append('}');
        return sb.toString();
    }
}
