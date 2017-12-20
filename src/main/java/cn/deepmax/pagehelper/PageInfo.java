package cn.deepmax.pagehelper;

import java.util.ArrayList;
import java.util.List;


public class PageInfo<T> {

    private Integer nowPage;
    private Integer pageSize;
    private Long totalRow;
    private List<T> data = new ArrayList<>();
    private Long startRow;
    private Long endRow;
    private Integer totalPage;


    public PageInfo(Integer nowPage, Integer pageSize, Long totalRow) {
        if(nowPage==null||nowPage<=0){
            throw new IllegalArgumentException("nowPage should greater than zero.");
        }
        if(pageSize==null||pageSize<=0){
            throw new IllegalArgumentException("pageSize should greater than zero.");
        }
        if(totalRow==null||totalRow<0){
            throw new IllegalArgumentException("totalRow should >=0.");
        }
        this.nowPage = nowPage;
        this.pageSize = pageSize;
        this.totalRow = totalRow;
        startRow =  (long)(nowPage-1)*pageSize  ;       //start at 0
        totalPage = (totalRow.intValue()-1) / pageSize +1;
        endRow = startRow + pageSize-1;
        endRow = (endRow>totalRow-1)?totalRow-1:endRow;
    }


    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Long getTotalRow() {
        return totalRow;
    }

    public Long getStartRow() {
        return startRow;
    }

    public Long getEndRow() {
        return endRow;
    }

    public List<T> getData() {
        return data;
    }

    public Boolean isEmpty(){
        return data.isEmpty();
    }

    public Boolean isNotEmpty(){
        return !data.isEmpty();
    }

}
