import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.BirdAppController;
import Model.Post;
import Util.ConnectionUtil;
import io.javalin.Javalin;

public class CreatePostTest {
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
     * Sending an HTTP POST request to localhost:8080/posts with 
     * valid post
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of post object
     */
    @Test
    public void createPostSuccessful() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":1, " +
                        "\"post_content\": \"hello world\", " +
                        "\"time_posted_epoch\": 1714600418}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assert.assertEquals(200, status);        

        ObjectMapper om = new ObjectMapper();
        Post expectedResult = new Post(2, 1, "hello world", 1714600418);
        System.out.println(response.body().toString());
        Post actualResult = om.readValue(response.body().toString(), Post.class);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Sending an HTTP POST request to localhost:8080/posts with empty message
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void createPostPostTextBlank() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":1, " +
                        "\"post_content\": \"\", " +
                        "\"time_posted_epoch\": 1714600418}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(400, status);        
        Assert.assertEquals("", response.body().toString());
    }


    /**
     * Sending an http request to POST localhost:8080/messages with message length greater than 140
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void createPostPostGreaterThan255() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":1, " +
                        "\"post_content\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\", " +
                        "\"time_posted_epoch\": 1714600418}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        Assert.assertEquals(400, status);        
        Assert.assertEquals("", response.body().toString());
    }


    /**
     * Sending an http request to POST localhost:8080/posts with a user id that
     * doesnt exist in db
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void createPostUserNotInDb() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts"))
                .POST(HttpRequest.BodyPublishers.ofString("{"+
                        "\"posted_by\":69, " +
                        "\"post_content\": \"nice\", " +
                        "\"time_posted_epoch\": 1714600418}"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        Assert.assertEquals(400, status);        
        Assert.assertEquals("", response.body().toString());
    }


}
