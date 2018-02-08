package cn.deepmax.entity.entity;

import cn.deepmax.util.TypeAdapter;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TypeAdapterTest {

    private static final Logger logger = LoggerFactory.getLogger(TypeAdapterTest.class);
    @Test
    public void testAdapter(){
        Object e = null;
        Assert.isNull(TypeAdapter.getCompatibleValue(Integer.class,e),"test  null");
        Object lo = TypeAdapter.getCompatibleValue(long.class,5);
        Assert.isTrue(Long.class.isInstance(lo),"test integer to long");
        Boolean b = (Boolean)TypeAdapter.getCompatibleValue(Boolean.class,2);
        Assert.isTrue(b,"b is true");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Date date = (Date) TypeAdapter.getCompatibleValue(Date.class,now);
        Assert.isTrue(date.getTime()==now.getTime(),"time should be same");
        Timestamp now2 = (Timestamp)TypeAdapter.getCompatibleValue(Timestamp.class,date);
        Assert.isTrue(date.getTime()==now2.getTime(),"time should be same");
        LocalDateTime localDateTime =(LocalDateTime) TypeAdapter.getCompatibleValue(LocalDateTime.class,now);
        LocalDate localDate = (LocalDate)TypeAdapter.getCompatibleValue(LocalDate.class,now);
        Assert.isTrue(localDate.equals(localDateTime.toLocalDate()),"localdate");
        LocalDate localDate1 = (LocalDate) TypeAdapter.getCompatibleValue(LocalDate.class,date);
        Assert.isTrue(localDate.equals(localDate1),"localdate1");
        Object aa = TypeAdapter.getCompatibleValue(int.class,1);
        Assert.isTrue(Integer.class.isInstance(aa),"test int to Integer");
        Boolean i = (Boolean)TypeAdapter.getCompatibleValue(Boolean.class, 1);
        Assert.isTrue(i,"i should be true");
        Boolean i2 = (Boolean)TypeAdapter.getCompatibleValue(Boolean.class, 32.3);
        Assert.isTrue(!i2,"i2  should be false");
        BigDecimal decimal = (BigDecimal)TypeAdapter.getCompatibleValue(BigDecimal.class, 32.3D);
        Assert.isTrue(decimal !=null,"true");


    }

}
