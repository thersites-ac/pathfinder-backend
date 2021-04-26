package cogbog.dao;

public interface DaoData<K, V> {
    public K getId();
    public void superimpose(V updates);
}
