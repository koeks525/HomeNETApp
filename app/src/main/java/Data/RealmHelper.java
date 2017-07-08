package Data;

import android.app.Application;

import java.io.File;
import java.util.List;

import Models.Category;
import Models.Country;
import Models.Key;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Okuhle on 2017/07/04.
 */

public class RealmHelper {

    private Realm realmInstance;

    public RealmHelper() {
        realmInstance = Realm.getDefaultInstance();
    }

    public boolean addCountries(List<Country> newCountry) {
        for (Country countryObject : newCountry) {
            realmInstance.beginTransaction();
            Country country = realmInstance.createObject(Country.class, countryObject.getCountryID());
            country.setName(countryObject.getName());
            country.setIsDeleted(countryObject.getIsDeleted());
            realmInstance.commitTransaction();
        }
        return true;
    }

    public boolean exists() {
        if (realmInstance.getConfiguration().getRealmDirectory().exists()) {
            List<Country> countryList = getCountries();
            List<Key> keyList = getKeys();
            List<Category> categoryList = getCategories();
            if (countryList == null || keyList == null || categoryList == null) {
                return false;
            }
            if (countryList.size() == 0 || keyList.size() == 0 || categoryList.size() == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean addKeys(List<Key> keyList) {
        for (Key thisKey : keyList) {
            realmInstance.beginTransaction();
            Key key = realmInstance.createObject(Key.class, thisKey.getKeyID());
            key.setName(thisKey.getName());
            key.setDescription(thisKey.getDescription());
            key.setIsDeleted(thisKey.getIsDeleted());
            key.setValue(thisKey.getValue());
            realmInstance.commitTransaction();
        }
        return true;
    }

    public boolean addCategories(List<Category> categories) {
        for (Category category : categories) {
            realmInstance.beginTransaction();
            Category thisOne = realmInstance.createObject(Category.class, category.getCategoryID());
            thisOne.setName(category.getName());
            thisOne.setIsDeleted(category.getIsDeleted());
            realmInstance.commitTransaction();
        }
        return true;
    }

    public List<Category> getCategories(){
        RealmResults<Category> results =  realmInstance.where(Category.class).findAllAsync();
        List<Category> categoryList = realmInstance.copyFromRealm(results);
        return categoryList;
    }

    public List<Country> getCountries() {
        RealmResults<Country> results = realmInstance.where(Country.class).findAllAsync();
        List<Country> countryList = realmInstance.copyFromRealm(results);
        return countryList;

    }

    public List<Key> getKeys() {
        RealmResults<Key> results = realmInstance.where(Key.class).findAllAsync();
        List<Key> keys = realmInstance.copyFromRealm(results);
        return keys;

    }
}
