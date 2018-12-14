package cn.deepmax.easyquery.entity.adapter;

public enum  EnumType {



    TYPE1(100,"OK"),TYPE2(200,"OK200"),TYPE3(300,"OK300");
    private int index;
    private String msg;
    EnumType(int i, String m){
        index = i;
        msg = m;
    }

    public int getIndex() {
        return index;
    }



    public String getMsg() {
        return msg;
    }


}
