package cn.deepmax.easyquery.model;

/**
 * tuple.
 * @param <U>
 * @param <V>
 */
public class Pair<U,V> {
    public U first;
    public V last;
    public Pair(U ff,V ll){
        first=ff;
        last=ll;
    }
}
