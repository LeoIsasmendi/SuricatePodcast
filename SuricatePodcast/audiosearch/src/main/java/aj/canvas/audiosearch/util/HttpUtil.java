package aj.canvas.audiosearch.util;

import aj.canvas.audiosearch.model.JsonConverter;
import retrofit2.Converter;

import java.io.UnsupportedEncodingException;

public class HttpUtil {

    public static Converter.Factory getJsonConverter(JsonConverter jsonConverter){
        Converter.Factory converter = null;
        switch (jsonConverter){
            case Gson:
                converter = null;
                break;
            case Jackson:
                converter = null;
                break;
            case Moshi:
                converter = null;
                break;
            case Protobuf:
                converter = null;
                break;
            case Wire:
                converter = null;
                break;
            case SimpleXML:
                converter = null;
                break;
        }

        return converter;
    }

    public static String getSignature(String applicationId, String secret) throws UnsupportedEncodingException{
        String unencoded = applicationId+":"+ secret;
        String signature = Base64Encode.encode(unencoded).replaceAll("(\\r|\\n)", "");
        return signature;
    }
    
}
