package cn.deepmax.entity.entity;

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
}
