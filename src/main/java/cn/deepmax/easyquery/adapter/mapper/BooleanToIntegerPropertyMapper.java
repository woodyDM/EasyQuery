package cn.deepmax.easyquery.adapter.mapper;

public class BooleanToIntegerPropertyMapper implements PropertyMapper<Boolean, Integer> {


    private static BooleanToIntegerPropertyMapper INSTANCE = null;


    public static BooleanToIntegerPropertyMapper getInstance(){
        if(INSTANCE==null){
            synchronized (BooleanToIntegerPropertyMapper.class){
                if(INSTANCE==null){
                    INSTANCE = new BooleanToIntegerPropertyMapper();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        if(attribute==null)
            return null;
        else
            return attribute ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        if(dbData==null)
            return null;
        else{
            if(dbData==0 || dbData==1){
                return dbData==1 ? Boolean.TRUE : Boolean.FALSE;
            }else{
                throw new IllegalArgumentException("unable to map "+dbData+" to boolean value.it must be 1 or 0");
            }
        }

    }

    /**
     * for stateless mapper ,return unique name for cache key.
     *
     * @return
     */
    @Override
    public String getUniqueMapperName() {
        return "Default_BooleanToIntegerPropertyMapper";
    }
}
