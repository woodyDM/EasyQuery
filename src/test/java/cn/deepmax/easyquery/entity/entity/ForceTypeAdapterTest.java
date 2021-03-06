package cn.deepmax.easyquery.entity.entity;

import cn.deepmax.easyquery.util.ForceTypeAdapter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ForceTypeAdapterTest {

    private static final Logger logger = LoggerFactory.getLogger(ForceTypeAdapterTest.class);
    @Test
    public void testAdapter(){

        Object e = null;
        Assert.isNull(ForceTypeAdapter.doGetCompatibleValue(Integer.class,e),"test  null");
        Object lo = ForceTypeAdapter.doGetCompatibleValue(long.class,5);
        Assert.isTrue(lo instanceof Long,"test integer to long");
        Boolean b = (Boolean) ForceTypeAdapter.doGetCompatibleValue(Boolean.class,2);
        Assert.isTrue(b,"b is true");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Date date = (Date) ForceTypeAdapter.doGetCompatibleValue(Date.class,now);
        Assert.isTrue(date.getTime()==now.getTime(),"time should be same");
        Timestamp now2 = (Timestamp) ForceTypeAdapter.doGetCompatibleValue(Timestamp.class,date);
        Assert.isTrue(date.getTime()==now2.getTime(),"time should be same");
        LocalDateTime localDateTime =(LocalDateTime) ForceTypeAdapter.doGetCompatibleValue(LocalDateTime.class,now);
        LocalDate localDate = (LocalDate) ForceTypeAdapter.doGetCompatibleValue(LocalDate.class,now);
        Assert.isTrue(localDate.equals(localDateTime.toLocalDate()),"localdate");
        LocalDate localDate1 = (LocalDate) ForceTypeAdapter.doGetCompatibleValue(LocalDate.class,date);
        Assert.isTrue(localDate.equals(localDate1),"localdate1");
        Object aa = ForceTypeAdapter.doGetCompatibleValue(int.class,1);
        Assert.isTrue(aa instanceof Integer,"test int to Integer");
        Boolean i = (Boolean) ForceTypeAdapter.doGetCompatibleValue(Boolean.class, 1);
        Assert.isTrue(i,"i should be true");
        Boolean i2 = (Boolean) ForceTypeAdapter.doGetCompatibleValue(Boolean.class, 32.3);
        Assert.isTrue(!i2,"i2  should be false");
        BigDecimal decimal = (BigDecimal) ForceTypeAdapter.doGetCompatibleValue(BigDecimal.class, 32.3D);
        Assert.isTrue(decimal !=null,"true");


    }

}
