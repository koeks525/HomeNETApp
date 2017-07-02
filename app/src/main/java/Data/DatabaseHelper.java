package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Models.Category;
import Models.Country;
import Models.Key;
import Models.User;

/**
 * Created by Okuhle on 2017/03/31.
 */

public class DatabaseHelper  extends SQLiteOpenHelper{

    private static final String databaseName = "HomeNET_AppDB";
    private static final String countryTable = "Country";
    private static final String countryID = "CountryID";
    private static final String countryName = "Name";
    private static final String isDeleted = "IsDeleted";
    private static final String keyTable = "Key";
    private static final String keyID = "KeyID";
    private static final String description = "Description";
    private static final String value = "Value";
    private static final String userTable = "User";
    private static final String userId = "UserID";
    private static final String name = "Name";
    private static final String surname = "Surname";
    private static final String email = "Email";
    private static final String dateOfBirth = "DateOfBirth";
    private static final String userName = "UserName";
    private static final String password = "Password";
    private static final String securityQuestion = "SecurityQuestion";
    private static final String securityAnswer = "SecurityAnswer";
    private static final String dateRegistered = "DateRegistered";
    private static final String gender = "Gender";
    private static final String phoneNumber = "PhoneNumber";
    private static final String skypeID = "SkypeID";
    private static final String facebookID = "FacebookID";
    private static final String twitterID = "TwitterID";
    private static final String passwordSalt = "PasswordSalt";
    private static final String profileImage = "ProfileImage";


    public DatabaseHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + countryTable + "("+ countryID + " INTEGER PRIMARY KEY, "+ countryName +" TEXT, "+ isDeleted +" INTEGER);");

        sqLiteDatabase.execSQL("CREATE TABLE " + keyTable + "("+keyID +" INTEGER PRIMARY KEY, "+description +" TEXT, "+ value + " TEXT, "+isDeleted +" INTEGER, "+countryName +" TEXT);" );

        sqLiteDatabase.execSQL("CREATE TABLE User (UserID INTEGER PRIMARY KEY, Name TEXT, Email TEXT, Username TEXT, Password TEXT);");


        sqLiteDatabase.execSQL("CREATE TABLE Category (CategoryID INTEGER PRIMARY KEY, Name TEXT, IsDeleted INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ countryTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ keyTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+userTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Category");
    }

    public boolean insertCountries(List<Country> countries) {
        SQLiteDatabase database = this.getWritableDatabase();
        long isSuccessful;
        ContentValues contentValues;
        for (Country country : countries) {
            contentValues = new ContentValues();
            contentValues.put(countryID, country.getCountryID());
            contentValues.put(countryName, country.getName());
            contentValues.put(isDeleted, country.getIsDeleted());
            isSuccessful = database.insert(countryTable, null, contentValues);
            if (isSuccessful == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean insertKeys(List<Key> keys) {
        SQLiteDatabase database = this.getWritableDatabase();
        long isSuccessful;
        ContentValues contentValues;
        for (Key key : keys) {
            contentValues = new ContentValues();
            contentValues.put(keyID, key.getKeyID());
            contentValues.put(description, key.getDescription());
            contentValues.put(value, key.getValue());
            contentValues.put(isDeleted, key.getIsDeleted());
            contentValues.put(countryName, key.getName());
            isSuccessful = database.insert(keyTable, null, contentValues);
            if (isSuccessful == -1) {
                return false;
            }
        }

        return true;
    }

    public List<Category> getCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Category", null);
        while (cursor.moveToNext()) {
            categoryList.add(new Category(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
        }
        cursor.close();
        return categoryList;
    }

    public boolean insertCategories(List<Category> categoryList) {
        SQLiteDatabase database = this.getWritableDatabase();
        long isSuccessful;
        ContentValues values;
        for(Category category : categoryList) {
            values = new ContentValues();
            values.put("CategoryID", category.getCategoryID());
            values.put("Name", category.getName());
            values.put("IsDeleted", category.getIsDeleted());
            isSuccessful = database.insert("Category", null, values);
            if (isSuccessful == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean insertUser(User newUser) {
        SQLiteDatabase database = this.getWritableDatabase();
        long isSuccessful;
        ContentValues contentValues = new ContentValues();
        contentValues.put(userId, newUser.getId());
        contentValues.put(name, newUser.getName());
        contentValues.put(email, newUser.getEmail());
        contentValues.put(userName, newUser.getUserName());
        contentValues.put(password, newUser.getPassword());
        isSuccessful = database.insert(userTable, null, contentValues);
        if (isSuccessful != -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUser(User newUser) {
        SQLiteDatabase database = this.getWritableDatabase();
        long isSuccessful;
        ContentValues contentValues = new ContentValues();
        contentValues.put(userId, newUser.getId());
        contentValues.put(name, newUser.getName());
        contentValues.put(surname, newUser.getSurname());
        contentValues.put(email, newUser.getEmail());
        contentValues.put(dateOfBirth, newUser.getDateOfBirth());
        contentValues.put(userName, newUser.getUserName());
        contentValues.put(password, newUser.getPassword());
        contentValues.put(securityQuestion, newUser.getSecurityQuestion());
        contentValues.put(securityAnswer, newUser.getSecurityAnswer());
        contentValues.put(isDeleted, newUser.getIsDeleted());
        contentValues.put(dateRegistered, newUser.getDateRegistered());
        contentValues.put(gender, newUser.getGender());
        contentValues.put(countryID, newUser.getCountryID());
        contentValues.put(phoneNumber, newUser.getPhoneNumber());
        contentValues.put(skypeID, newUser.getSkypeID());
        contentValues.put(facebookID, newUser.getFacebookID());
        contentValues.put(twitterID, newUser.getTwitterID());
        contentValues.put(profileImage, newUser.getProfileImage());
        isSuccessful = database.update(userTable, contentValues, userId + " = "+newUser.getId(), null);
        if (isSuccessful != -1) {
            return true;
        } else {
            return false;
        }
    }

    public List<Country> getCountries() {
        List<Country> countryList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Country", null);
        while (cursor.moveToNext()) {
                Country currentCountry = new Country(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
                countryList.add(currentCountry);
            }
            cursor.close();
            return countryList;

    }

    public List<Key> getKeys() {
        List<Key> keyList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Key", null);
        while (cursor.moveToNext()) {
            Key currentKey = new Key(cursor.getInt(0), cursor.getString(4), cursor.getString(3), cursor.getString(2), cursor.getInt(1));
            keyList.add(currentKey);
        }
        cursor.close();
        return keyList;
    }

    //Source: http://stackoverflow.com/questions/3386667/query-if-android-database-exists
    public static boolean checkDatabase(Context context, String databaseName) {
        File databaseFile = context.getDatabasePath(databaseName);
        return databaseFile.exists();
    }


}
