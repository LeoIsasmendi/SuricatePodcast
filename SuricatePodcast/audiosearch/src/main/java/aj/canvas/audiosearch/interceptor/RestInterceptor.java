package aj.canvas.audiosearch.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RestInterceptor implements Interceptor {
    private String AccessToken;

    public RestInterceptor(String accessToken){
        this.AccessToken = accessToken;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer " + AccessToken)
                .build();
        return chain.proceed(newRequest);
    }
}
