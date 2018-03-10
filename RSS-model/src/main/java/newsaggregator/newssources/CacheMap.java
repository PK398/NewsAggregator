package newsaggregator.newssources;

import java.util.LinkedHashMap;

public class CacheMap<K,V> extends LinkedHashMap<K,V> {

  private static CacheMap INSTANCE = new CacheMap();


  private CacheMap() {
  }

  public static CacheMap getInstance() {
    return INSTANCE;
  }
}
