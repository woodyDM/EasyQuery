package cn.deepmax.entity.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class SuperUser extends User {

    private BigDecimal bigDecimal;
    private Date updateDate;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SuperUser{");
        sb.append("bigDecimal=").append(bigDecimal);
        sb.append(", updateDate=").append(updateDate);
        sb.append('}');
        return sb.toString();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }
}
