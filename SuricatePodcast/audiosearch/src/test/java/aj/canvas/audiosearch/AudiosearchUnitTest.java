package aj.canvas.audiosearch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import aj.canvas.audiosearch.exception.CredentialsNotFoundException;
import aj.canvas.audiosearch.model.AuthResult;
import aj.canvas.audiosearch.model.EpisodeResult;
import aj.canvas.audiosearch.model.TrendResult;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@RunWith(MockitoJUnitRunner.class)
public class AudiosearchUnitTest {

    private Audiosearch audioSearch;
    /**
     *Test App credentials - Get yours at https://audiosear.ch/developers
     **/

    private final String APPLICATION_ID = "YOUR_APPLICATION_ID";
    private final String SECRET = "YOUR_SECRET_KEY";

    @Test
    public void AccessToken_isNotNull() throws IOException, CredentialsNotFoundException{
        setUp();
        AuthResult accessToken = audioSearch.getAccessToken();
        assertNotNull(accessToken.accessToken);
    }

    @Test
    public void Trending_isNotEmpty() throws Exception{
        setUp();
        assertNotEquals(0, audioSearch.getTrending().execute().body().size());
    }

    @Test
    public void EpisodesSearchResult_isNotEmpty() throws Exception {
        setUp();
        assertNotEquals(0, audioSearch.searchEpisodes("elections").execute().body().getResults().size());
    }

    @Test
    public void Episode_isNotNull() throws Exception{
        setUp();
        assertNotNull(audioSearch.getEpisode(67946).execute().body().getItunesEpisode());
    }

    @Test
    public void TranscriptSearchResult_isNotEmpty(){

    }

    @Test
    public void PeopleSearchResult_isNotEmpty(){

    }

    public void setUp() throws CredentialsNotFoundException, UnsupportedEncodingException {

        audioSearch = new Audiosearch()
                        .setApplicationId(APPLICATION_ID)
                        .setSecret(SECRET)
                        .build();
    }

    public void Authorize() throws CredentialsNotFoundException, UnsupportedEncodingException{
        setUp();
    }
}