package Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Okuhle on 2017/03/02.
 */

public class User implements Parcelable{

    @SerializedName("id")
    private int id;
    @SerializedName("surname")
    private String surname;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("dateOfBirth")
    private String dateOfBirth;
    @SerializedName("userName")
    private String userName;
    @SerializedName("password")
    private String password;
    @SerializedName("securityQuestion")
    private String securityQuestion;
    @SerializedName("securityAnswer")
    private String securityAnswer;
    @SerializedName("profileImage")
    private String profileImage;
    @SerializedName("dateRegistered")
    private String dateRegistered;
    @SerializedName("isDeleted")
    private int isDeleted;
    @SerializedName("gender")
    private String gender;
    @SerializedName("countryID")
    private int countryID;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("skypeID")
    private String skypeID;
    @SerializedName("facebookID")
    private String facebookID;
    @SerializedName("twitterID")
    private String twitterID;

    public User() {}
    public User(int id, String surname, String name, String email, String dateOfBirth, String userName, String password, String securityQuestion, String securityAnswer, String dateRegistered, int isDeleted, String gender, int countryID, String phoneNumber) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.userName = userName;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.dateRegistered = dateRegistered;
        this.isDeleted = isDeleted;
        this.gender = gender;
        this.countryID = countryID;
        this.phoneNumber = phoneNumber;
    }

    protected User(Parcel in) {
        id = in.readInt();
        surname = in.readString();
        name = in.readString();
        email = in.readString();
        dateOfBirth = in.readString();
        userName = in.readString();
        password = in.readString();
        securityQuestion = in.readString();
        securityAnswer = in.readString();
        profileImage = in.readString();
        dateRegistered = in.readString();
        isDeleted = in.readInt();
        gender = in.readString();
        countryID = in.readInt();
        phoneNumber = in.readString();
        skypeID = in.readString();
        facebookID = in.readString();
        twitterID = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSkypeID() {
        return skypeID;
    }

    public void setSkypeID(String skypeID) {
        this.skypeID = skypeID;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    public String getTwitterID() {
        return twitterID;
    }

    public void setTwitterID(String twitterID) {
        this.twitterID = twitterID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(surname);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(dateOfBirth);
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(securityQuestion);
        parcel.writeString(securityAnswer);
        parcel.writeString(profileImage);
        parcel.writeString(dateRegistered);
        parcel.writeInt(isDeleted);
        parcel.writeString(gender);
        parcel.writeInt(countryID);
        parcel.writeString(phoneNumber);
        parcel.writeString(skypeID);
        parcel.writeString(facebookID);
        parcel.writeString(twitterID);
    }
}
