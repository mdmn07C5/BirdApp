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

public class DeletePostByIdTest {
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
     * Sending an HTTP DELETE request to localhost:8080/posts/1 an existing post
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of the post that was deleted
     */
    @Test
    public void deletePostGivenPostIdPostFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);

        Post expectedResult = new Post(1, 1,"test message from Apu",1714600414);
        Post actualResult = objectMapper.readValue(response.body().toString(), Post.class);
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Sending an HTTP DELETE request to localhost:8080/posts/69 (nice)
     * post # 69 does not exist in db
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: 
     */
    @Test
    public void deletePostGivenPostIdPostNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/posts/69"))
                .DELETE()
                .build();
        HttpResponse<String> response = webClient.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);
        Assert.assertTrue(response.body().toString().isEmpty());
    }
    
}
