import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.BirdAppController;
import Model.Post;
import Util.ConnectionUtil;
import io.javalin.Javalin;

public class RetrieveAllPostsTest {
    BirdAppController birdAppController;
    HttpClient webClient;
    ObjectMapper objectMapper;
    Javalin app;

    /**
     * Before every test, start with a blank slate
     * @throws InterruptedException
     */
    @Before
    public void setUp() throws InterruptedException {
        ConnectionUtil.resetTestDatabase();
        birdAppController = new BirdAppController();
        app = birdAppController.startAPI();
        webClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        app.start(8080);
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        app.stop();
    }

    /**
     * Sending an HTTP GET request to localhost:8080/posts 
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON represenation of a list of message objects
     */
    @Test
    public void getAllPostsPostsAvailable() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);

        List<Post> expectedResult = new ArrayList<>();
        expectedResult.add(new Post(1, 1,"test message from Apu",1714600414));
        expectedResult.add(new Post(2, 2,"test message from Spurdo",1714600416));
        List<Post> actualResult = objectMapper.readValue(response.body().toString(), new TypeReference<List<Post>>(){});
        Assert.assertEquals(expectedResult, actualResult);
    }


    /**
     * Sending an HTTP GET request to localhost:8080/posts with no mesages in db
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON represenation of an empty list
     */
    @Test
    public void getAllPostsNoPosts() throws IOException, InterruptedException {
        
        removeInitialPost();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);

        List<Post> posts = objectMapper.readValue(response.body().toString(), new TypeReference<List<Post>>(){});
        Assert.assertTrue(posts.isEmpty());
    }



    private void removeInitialPost(){
        try {
                Connection conn = ConnectionUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement("truncate table post");
                ps.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

}
