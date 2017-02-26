package aj.canvas.audiosearch.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthorizationInterceptor implements Interceptor {

    private String Signature;

    public AuthorizationInterceptor(String signature){
        this.Signature = signature;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request.newBuilder()
                .addHeader("Authorization", "Basic " + this.Signature)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        return chain.proceed(request);
    }

    public void setSignature(String signature){
        this.Signature = signature;
    }
}
