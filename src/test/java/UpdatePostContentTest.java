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

public class UpdatePostContentTest {
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
     * Sending an HTTP PATCH request to localhost:8080/posts/1 
     * (post id exists in db) with valid post content
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of the post that was updated
     */
    @Test
    public void updatePostSuccessful() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"post_content\": \"updated post\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);        

        ObjectMapper om = new ObjectMapper();
        Post expectedResult = new Post(1, 1, "updated post", 1714600414);

        Post actualResult = om.readValue(response.body().toString(), Post.class);
        Assert.assertEquals(expectedResult, actualResult);
    }


    /**
     * Sending an HTTP PATCH request to localhost:8080/posts/69 (nice) 
     * post #69 does not exist in db
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void updatePostPostNotFound() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/69"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"post_content\": \"updated post\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        Assert.assertEquals(400, status);        
        Assert.assertTrue(response.body().toString().isEmpty());
    }


    /**
     * Sending an HTTP PATCH request to localhost:8080/posts/1 
     * with an empty string 
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void updatePostPostStringEmpty() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"post_content\": \"\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        
        Assert.assertEquals(400, status);        
        Assert.assertTrue(response.body().toString().isEmpty());
    }


    /**
     * Sending HTTP PATCH request to localhost:8080/posts/1 
     * with a post content greater than 140 characters 
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void updatePostPostTooLong() throws IOException, InterruptedException {
        HttpRequest postPostRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1"))
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{"+
                        "\"post_content\": \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postPostRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(400, status);        
        Assert.assertTrue(response.body().toString().isEmpty());
    }
}
