package aj.canvas.audiosearch.service;

import aj.canvas.audiosearch.model.AuthResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface AuthorizationService {

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("oauth/token")
    Call<AuthResult> getAccessToken(@Field("grant_type")  String grantType,  @Header("Authorization") String authorizationSignature);
}
