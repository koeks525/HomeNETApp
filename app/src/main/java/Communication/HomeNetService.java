package Communication;

import java.io.File;
import java.util.List;

import Models.Category;
import Models.Country;
import Models.HomeData;
import Models.House;
import Models.HouseMember;
import Models.HousePost;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.Key;
import Models.LoginViewModel;
import Models.Organization;
import Models.SearchViewModel;
import Models.Token;
import Models.User;
import Models.UserData;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Okuhle on 2017/03/02.
 */

public interface HomeNetService {

    @GET("Country/GetCountries")
    Call<ListResponse<Country>> getCountries(@Query("clientCode") String clientCode);
    @POST("User/CreateUser")
    Call<SingleResponse<User>> createUser(@Body User newUser, @Query("clientCode") String clientCode);
    @GET("Key/GetKeys")
    Call<ListResponse<Key>> getKeys(@Query("clientCode") String clientCode);
    @POST("User/CreateToken")
    Call<SingleResponse<Token>> createToken(@Body LoginViewModel model, @Query("clientCode") String clientCode);
    @GET("User/RemoveUser")
    Call<SingleResponse<User>> removeUser(@Query("userId") int userId);
    @POST("User/Login")
    Call<SingleResponse<User>> loginUser(@Header("Authorization") String authCode, @Body LoginViewModel model, @Query("clientCode") String clientCode);
    @Multipart
    @POST("House/CreateHouse")
    Call<SingleResponse<House>> createHouse(@Header("Authorization") String authCode, @Part("houseName") RequestBody houseName, @Part("description") RequestBody description, @Part("houseLocation") RequestBody houseLocation, @Part("emailAddress") RequestBody emailAddress, @Query("clientCode") String clientCode, @Part MultipartBody.Part imageFile);
    @GET("Category/GetCategories")
    Call<ListResponse<Category>> getCategories(@Query("clientCode") String clientCode);
    @GET("House/GetHouses")
    Call<ListResponse<House>> getUserHouses(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("User/ForgotPassword")
    Call<SingleResponse<User>> forgotPassword(@Query("emailAddress") String emailAddress, @Query("dateOfBirth") String dateOfBirth, @Query("clientCode") String clientCode);
    @GET("User/GetHousePosts")
    Call<ListResponse<HousePost>> getUserPosts(@Header("Authorization") String authCode, @Query("userID") int userID, @Query("clientCode") String clientCode);
    @GET("Organization/GetUserOrganizations")
    Call<ListResponse<Organization>> getUserOrganizations(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("User/GetHousePosts")
    Call<ListResponse<HousePost>> getHousePosts(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @POST("User/RegisterFirebaseToken")
    Call<SingleResponse<String>> registerFirebaseToken(@Header("Authorization") String authCode, @Part("emailAddress") RequestBody emailAddress, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetActiveHouseMembers")
    Call<ListResponse<HouseMember>> getActiveHouseMembers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetBannedHouseMembers")
    Call<ListResponse<HouseMember>> getBannedHouseUsers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetPendingHouseMembers")
    Call<ListResponse<HouseMember>> getPendingHouseUsers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
    @GET("User/GetUserById")
    Call<SingleResponse<User>> getUserById(@Header("Authorization") String authCode, @Query("userId") int userId, @Query("clientCode") String clientCode);
    @GET("House/GetHouse")
    Call<SingleResponse<House>> getHouse(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("House/GetHouseProfileImage")
    Call<SingleResponse<File>> getHouseImage(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @POST("House/SearchHouses")
    Call<ListResponse<House>> searchHouses(@Header("Authorization") String authCode, @Body SearchViewModel searchParameter, @Query("clientCode") String clientCode);
    @POST("House/JoinHouse")
    Call<SingleResponse<HouseMember>> joinHouse(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("HousePostMetaData/GetPostData")
    Call<SingleResponse<HousePostMetaDataViewModel>> getHousePostMetaData(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @POST("HousePostMetaData/RegisterLike")
    Call<SingleResponse<HousePostMetaData>> registerLike(@Header("Authorization") String authCode, @Body String emailAddress,@Query("clientCode") String clientCode );
    @POST("HousePostMetaData/RegisterDislike")
    Call<SingleResponse<HousePostMetaData>> registerDislike(@Header("Authorization") String authCode, @Body String emailAddress, @Query("clientCode") String clientCode);
    @GET("Report/GetHouseOverviewReport")
    Call<SingleResponse<HomeData>> getHouseOverviewReport(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("Report/GetUserOverviewReport")
    Call<SingleResponse<UserData>> getUserOverviewReport(@Header("Authorization") String authCode, @Part("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetHouseMember")
    Call<SingleResponse<HouseMember>> getHouseMember(@Header("Authorization") String authCode, @Query("houseMemberID") int houseMemberID, @Query("clientCode") String clientCode);
    @GET("User/GetUserMemberships")
    Call<ListResponse<HouseMember>> getUserMemberships(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @Multipart
    @POST("HousePost/AddHousePost")
    Call<SingleResponse<HousePost>> addHousePost(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Part("emailAddress") RequestBody emailAddress, @Part("postText") RequestBody postText, @Part("location") RequestBody location, @Query("clientCode") String clientCode, @Part MultipartBody.Part file );
}
