package ntk.ambrose.landscapist;

import java.util.HashMap;

public class Storage {
    private static Storage instance;
    private HashMap<String,Object> store;
    private Storage(){
        store = new HashMap<>();
    }
    public static Storage getInstance(){
        if(instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void putData(String key, Object data){
        store.put(key,data);
    }
    public <T> T getData(String key){
        return  (T)store.get(key);
    }

    public void remove(String key){
        store.remove(key);
    }
    public void removeAll(){
        store.clear();
    }
}
