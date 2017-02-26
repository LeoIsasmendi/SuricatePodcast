package aj.canvas.audiosearch;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import aj.canvas.audiosearch.exception.CredentialsNotFoundException;
import aj.canvas.audiosearch.model.AuthResult;
import aj.canvas.audiosearch.model.EpisodeQueryResult;
import aj.canvas.audiosearch.model.EpisodeResult;
import aj.canvas.audiosearch.model.Filter;
import aj.canvas.audiosearch.model.ShowQueryResult;
import aj.canvas.audiosearch.model.TrendResult;
import aj.canvas.audiosearch.service.AudiosearchService;
import aj.canvas.audiosearch.service.AuthorizationService;
import aj.canvas.audiosearch.util.HttpUtil;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Audiosearch {

    private String ApplicationId;
    private String Secret;
    private Retrofit asInstance;
    private Retrofit authInstance;
    private AudiosearchService restService;
    private AuthorizationService authorizationService;
    private final String AUDIOSEARCH_BASE_AUTH_URL = "https://www.audiosear.ch/";
    private final String AUDIOSEARCH_BASE_API_URL = "https://www.audiosear.ch/api/";
    private final String GRANT_TYPE = "client_credentials";
    private String AUTH_SIGNATURE;
    private String AUTH_TOKEN;
    private String AccessToken;
    private String Signature;
    private final String TAG = "AudioSearch";

    public Audiosearch(String applicationId, String secret){
        this.ApplicationId = applicationId;
        this.Secret = secret;
    }

    public Audiosearch(){

    }

    public Audiosearch setApplicationId(String applicationId){
        this.ApplicationId = applicationId;
        return this;
    }

    public Audiosearch setSecret(String secret){
        this.Secret = secret;
        return this;
    }

    public Audiosearch setSignature(String signature){
        this.Signature = signature;
        return this;
    }

    public AuthResult getAccessToken() throws IOException {
        return authorizationService.getAccessToken(GRANT_TYPE, AUTH_SIGNATURE).execute().body();
    }

    public void Authorize() throws IOException{
        AuthResult authResult = authorizationService.getAccessToken(GRANT_TYPE, AUTH_SIGNATURE).execute().body();
        this.AccessToken = authResult.accessToken;
        this.AUTH_TOKEN  = "Bearer" + " " + this.AccessToken;
    }

    public Audiosearch build() throws CredentialsNotFoundException, UnsupportedEncodingException {

        if (this.ApplicationId != null && this.Secret != null){

            if (this.Signature == null){
                this.Signature = HttpUtil.getSignature(this.ApplicationId, this.Secret);
                AUTH_SIGNATURE = "Basic " + this.Signature;
            }
        }
        else {
            if (this.Signature ==  null){
                throw new CredentialsNotFoundException("Please set up your application credentials / signature");
            }
            else {
                AUTH_SIGNATURE = "Basic " + this.Signature;
            }
        }

        // Initialize AuthorizationClient with Authorization interceptor
        OkHttpClient authClient = new OkHttpClient();

        // Initialize RestClient with Rest interceptor
        OkHttpClient asClient = new OkHttpClient();

        // Initialize Retrofit for Authentication
        authInstance = new Retrofit.Builder()
                            .baseUrl(AUDIOSEARCH_BASE_AUTH_URL)
                            .client(authClient)
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();

        // Initiliaze Retrofit with Jackson as a default converter, and Rest Interceptor
        asInstance = new Retrofit.Builder()
                .baseUrl(AUDIOSEARCH_BASE_API_URL)
                .client(asClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Initialize Retrofit's AuthorizationService
        authorizationService = authInstance.create(AuthorizationService.class);

        // Initialize Retrofit RestService
        restService = asInstance.create(AudiosearchService.class);
        return this;
    }


    public String getApplicationId(){
        return this.ApplicationId;
    }

    // Episode list by Tasttmakers
    public Call<List<Object>> getEpisodeList(int n){
        return restService.getTastemakersEpisodes(n);
    }

    // Episodes list by a specific tasktemaker
    public Call<List<Object>> getEpisodeList(String tastemakerId, int n){
        return restService.getEpisodesByTaskmaker(tastemakerId, n);
    }

    public Call<EpisodeQueryResult> searchEpisodes(String query) throws Exception{
        Authorize();
        return restService.searchEpisodes(query,AUTH_TOKEN);
    }

    // get episode details
    public Call<EpisodeResult> getEpisode(long episodeId) throws IOException{
        Authorize();
        return restService.getEpisode(episodeId,AUTH_TOKEN);
    }

    // search for shows, episodes and people
    public Call<List<Object>> search(String query, Filter filter){

        switch (filter){
            case Show:
                break;
            case Episode:
                break;
            case People:
                break;
        }

        return null;
    }

    // get related episodes
    public Call<List<Object>> getRelatedEpisodes(long episodeId){
        return restService.getRelatedEpisodes(episodeId);
    }

    // get a specific moment whithin an episode
    public Call<Object> getEpisode(long id, long timestamp){
        return restService.getEpisodeMoment(id, timestamp);
    }

    // get shows
    public Call<ShowQueryResult> searchShows(String query) throws Exception{
        Authorize();
        return restService.getShowList(query,AUTH_TOKEN);
    }

    // get a list of shows form tastemakers
    public Call<List<Object>> getShowList(int n){
        return restService.getShowsbyTastemakers(n);
    }

    // get show details
    public Call<Object> getShow(long showId){
        return restService.getShow(showId);
    }

    // get related shows
    public List<Object> getRelatedShows(long showId){
        return null;
    }

    // get person details
    public Call<Object> getPerson(long personId){
        return restService.getPerson(personId);
    }

    // get iTunes top-chart
    public Call<List<Object>> getDailyChart(){
        return restService.getDailyChart();
    }

    // get Trending

    public Call<List<TrendResult>> getTrending() throws IOException{
        Authorize();
        return restService.getTrending(AUTH_TOKEN);
    }

}
