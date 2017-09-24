package Communication;

import java.io.File;
import java.util.List;

import Models.AnnouncementComment;
import Models.AnnouncementCommentViewModel;
import Models.Category;
import Models.CommentPartialModel;
import Models.CommentViewModel;
import Models.Country;
import Models.HomeData;
import Models.House;
import Models.HouseAnnouncement;
import Models.HouseMember;
import Models.HouseMemberViewModel;
import Models.HousePost;
import Models.HousePostFlag;
import Models.HousePostMetaData;
import Models.HousePostMetaDataViewModel;
import Models.HousePostViewModel;
import Models.HouseViewModel;
import Models.Key;
import Models.LoginViewModel;
import Models.MessageThread;
import Models.MessageThreadMessage;
import Models.MessageViewModel;
import Models.MessagesViewModel;
import Models.NewAnnouncementViewModel;
import Models.NewCommentViewModel;
import Models.NewMessageThreadViewModel;
import Models.Organization;
import Models.PostDetailsViewModel;
import Models.SearchViewModel;
import Models.Token;
import Models.User;
import Models.UserData;
import Models.UserViewModel;
import ResponseModels.ListResponse;
import ResponseModels.SingleResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by Okuhle on 2017/03/02.
 */

public interface HomeNetService {

    @GET("HousePost/GetHousePostData")
    Call<SingleResponse<HousePostViewModel>> getHousePostData(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @GET("HousePost/GetHousePost")
    Call<SingleResponse<HousePost>> getHousePost(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetBannedHouseMembers")
    Call<ListResponse<HouseMemberViewModel>> getBannedHouseMembers(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("adminEmail") String adminEmail, @Query("clientCode") String clientCode);
    @GET("HouseMember/DeclineHouseMember")
    Call<SingleResponse<HouseMember>> declineHouseMember(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("emailAddress") String emailAddress, @Query("adminEmail") String adminEmail, @Query("clientCode") String clientCode);
    @GET("HouseMember/ApproveHouseMember")
    Call<SingleResponse<HouseMember>> approveHouseMember(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("emailAddress") String emailAddress, @Query("adminEmail") String adminEmail, @Query("clientCode") String clientCode);
    @Streaming
    @GET("HousePost/GetHousePostImage")
    Call<ResponseBody> getHousePostImage(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @GET("HousePost/GetAllMultimediaPosts")
    Call<ListResponse<HousePostViewModel>> getAllMultimediaPosts(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("HousePostMetaData/GetPostDetails")
    Call<SingleResponse<PostDetailsViewModel>> getPostDetails(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @GET("HousePost/FlagHousePost")
    Call<SingleResponse<HousePostFlag>> flagHousePost(@Header("Authorization") String authCode, @Query("clientCode") String clientCode, @Query("housePostID") int housePostID,RequestBody flagReason, RequestBody emailAddress);
    @Streaming
    @GET("House/GetHouseProfileImage")
    Call<ResponseBody> getHouseProfileImage(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @Streaming
    @GET("User/GetProfilePicture")
    Call<ResponseBody> getProfilePicture(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode );
    @Multipart
    @POST("User/UpdateProfilePicture")
    Call<SingleResponse<User>> updateProfilePicture(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Part MultipartBody.Part imageFile, @Query("clientCode") String clientCode);
    @POST("Messages/AddMessageToThread")
    Call<SingleResponse<MessageThreadMessage>> addMessageToThread(@Header("Authorization") String authCode, @Body MessageViewModel messageModel, @Query("clientCode") String clientCode);
    @POST("Messages/CreateMessageThread")
    Call<SingleResponse<MessageThread>> createMessageThread(@Header("Authorization") String authCode, @Body NewMessageThreadViewModel newMessage, @Query("clientCode") String clientCode);
    @GET("Comment/GetComments")
    Call<ListResponse<CommentViewModel>> getComments(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @POST("Comment/AddComment")
    Call<SingleResponse<CommentViewModel>> addComment(@Header("Authorization") String authCode, @Body CommentPartialModel model, @Query("clientCode") String clientCode);
    @GET("HousePostMetaData/GetHousePostMetrics")
    Call<SingleResponse<HousePostMetaDataViewModel>> getHousePostMetrics(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Query("clientCode") String clientCode);
    @GET("HousePost/GetAllHousePosts")
    Call<ListResponse<HousePostViewModel>> getAllHousePosts(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @POST("AnnouncementComment/CreateAnnouncementComment")
    Call<SingleResponse<AnnouncementComment>> createAnnouncementComment(@Header("Authorization") String authCode, @Body NewCommentViewModel model, @Query("clientCode") String clientCode);
    @GET("AnnouncementComment/GetAnnouncementComments")
    Call<ListResponse<AnnouncementCommentViewModel>> getAnnouncementComments(@Header("Authorization") String authCode, @Query("houseAnnouncementID") int houseAnnouncementID, @Query("clientCode") String clientCode);
    @GET("Announcement/GetAllAnnouncements")
    Call<ListResponse<HouseAnnouncement>> getAllAnnouncements(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @POST("Announcement/CreateAnnouncement")
    Call<SingleResponse<HouseAnnouncement>> createAnnouncement(@Header("Authorization") String authCode, @Body NewAnnouncementViewModel model, @Query("clientCode") String clientCode);
    @GET("Announcement/GetUserAnnouncements")
    Call<ListResponse<HouseAnnouncement>> getUserAnnouncements(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @POST("User/UpdateUser")
    Call<SingleResponse<User>> updateUser(@Header("Authorization") String authCode, @Body User updateUser, @Query("clientCode") String clientCode);
    @GET("User/GetUser")
    Call<SingleResponse<User>> getUser(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String cleintCode);
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
    Call<SingleResponse<House>> createHouse(@Header("Authorization") String authCode, @Part("houseName") RequestBody houseName, @Part("description") RequestBody description,  @Part("emailAddress") RequestBody emailAddress, @Query("clientCode") String clientCode, @Part MultipartBody.Part imageFile);
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
    Call<ListResponse<HouseMemberViewModel>> getActiveHouseMembers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetBannedHouseMembers")
    Call<ListResponse<HouseMember>> getBannedHouseUsers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetPendingHouseMembers")
    Call<ListResponse<HouseMemberViewModel>> getPendingHouseUsers(@Header("Authorization") String authCode, @Query("houseID") int houseId, @Query("clientCode") String clientCode);
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
    Call<SingleResponse<HousePostMetaData>> registerLike(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Body String emailAddress,@Query("clientCode") String clientCode );
    @POST("HousePostMetaData/RegisterDislike")
    Call<SingleResponse<HousePostMetaData>> registerDislike(@Header("Authorization") String authCode, @Query("housePostID") int housePostID, @Body String emailAddress, @Query("clientCode") String clientCode);
    @GET("Report/GetHouseOverviewReport")
    Call<SingleResponse<HomeData>> getHouseOverviewReport(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @Multipart
    @POST("Report/GetUserOverviewReport")
    Call<SingleResponse<UserData>> getUserOverviewReport(@Header("Authorization") String authCode, @Part("emailAddress") RequestBody emailAddress, @Query("clientCode") String clientCode);
    @GET("HouseMember/GetHouseMember")
    Call<SingleResponse<HouseMember>> getHouseMember(@Header("Authorization") String authCode, @Query("houseMemberID") int houseMemberID, @Query("clientCode") String clientCode);
    @GET("User/GetUserMemberships")
    Call<ListResponse<HouseMember>> getUserMemberships(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @Multipart
    @POST("HousePost/AddHousePost")
    Call<SingleResponse<HousePost>> addHousePost(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Part("emailAddress") RequestBody emailAddress, @Part("postText") RequestBody postText, @Part("location") RequestBody location, @Query("clientCode") String clientCode, @Part MultipartBody.Part file );
    @GET("FlaggedPost/GetPendingHousePosts")
    Call<ListResponse<HousePostFlag>> getHousePendingPosts(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("FlaggedPost/GetFlaggedHousePosts")
    Call<ListResponse<HousePostFlag>> getFlaggedHousePosts(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("Announcement/GetHouseAnnouncements")
    Call<ListResponse<HouseAnnouncement>> getHouseAnnouncements(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @Multipart
    @POST("House/UpdateHouse")
    Call<SingleResponse<House>> updateHouse(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Part("houseName") String houseName, @Part("houseDescription") String houseDescription, @Part("emailAddress") String emailAddress, @Part("isPrivate") int isPrivate, @Part("oneTimePin") String oneTimePin, @Part MultipartBody.Part imageFile);
    @Multipart
    @POST("House/GetSubscribedHouses")
    Call<ListResponse<House>> getSubscribedHouses(@Header("Authorization") String authCode, @Part("emailAddress") RequestBody emailAddress, @Query("clientCode") String clientCode);
    @GET("Messages/GetMessageThread")
    Call<ListResponse<MessageThread>> getMessageThreads(@Header("Authorization") String authCode, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);
    @GET("Messages/GetMessagesInThread")
    Call<ListResponse<MessagesViewModel>> getMessagesInThread(@Header("Authorization") String authCode, @Query("messageThreadID") int messageThreadID, @Query("clientCode") String clientCode);
    @GET("House/GetUsersInHouse")
    Call<ListResponse<UserViewModel>> getUsersInHouse(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("House/GenerateHouseMetricsReport")
    Call<SingleResponse<HouseViewModel>> generateHouseMetricsReport(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("clientCode") String clientCode);
    @GET("House/LeaveHouse")
    Call<SingleResponse<HouseMember>> leaveHouse(@Header("Authorization") String authCode, @Query("houseID") int houseID, @Query("emailAddress") String emailAddress, @Query("clientCode") String clientCode);


}
