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
import Model.Account;
import Util.ConnectionUtil;
import io.javalin.Javalin;

public class UserLoginTest {

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
     * Sending an HTTP POST request to localhost:8080/account/login with 
     * valid username and password
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of user object
     */
    @Test
    public void loginSuccessful() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"apu_apustaja\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(200, status);
        ObjectMapper om = new ObjectMapper();
        Account expectedResult = new Account(1, "apu_apustaja", "password");
        Account actualResult = om.readValue(response.body().toString(), Account.class);
        Assert.assertEquals(expectedResult, actualResult);        

    }

    /**
     * Sending an HTTP POST request to localhost:8080/account/login with 
     * nonexistent account
     * 
     * Expected Response:
     *  Status Code: 401
     *  Response Body: 
     */
    @Test
    public void loginInvalidUsername() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"testuser404\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(401, status);
        Assert.assertEquals("", response.body().toString());

    }
    

    /**
     * Sending an HTTP POST request to localhost:8080/account/login with 
     * wrong password
     * 
     * Expected Response:
     *  Status Code: 401
     *  Response Body: 
     */
    @Test
    public void loginInvalidPassword() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/login"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"apu_apustaja\", " +
                        "\"password\": \"peepeepoopoo\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();

        Assert.assertEquals(401, status);
        Assert.assertEquals("", response.body().toString());

    }
}
