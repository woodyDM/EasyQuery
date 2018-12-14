package cn.deepmax.easyquery.test.adapter;

public enum MyColor {



    RED("红色"),BLACK("黑色"),WHITE("白色");
    private String desc;
    MyColor(String d){
        this.desc = d;
    }

    public String getDesc() {
        return desc;
    }

    public static MyColor parse(String st){
        for(MyColor it:values()){
            if(it.desc.equals(st)){
                return it;
            }
        }
        return null;
    }
}
