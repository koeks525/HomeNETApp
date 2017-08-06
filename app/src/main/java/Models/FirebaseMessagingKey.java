package Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Okuhle on 2017/08/04.
 */

public class FirebaseMessagingKey extends RealmObject {

    @PrimaryKey
    private String key;

    public FirebaseMessagingKey() {}
    public FirebaseMessagingKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
