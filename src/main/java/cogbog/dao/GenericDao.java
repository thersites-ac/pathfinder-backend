package cogbog.dao;

public interface GenericDao<K, V> {
    public K create(V value) throws Exception;

    public V find(K key);

    public V update(K key, V updates) throws Exception;

    public void delete(K key) throws Exception;
}
