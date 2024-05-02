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

public class UserRegistrationTest {
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
     * Send an HTTP POST request to localhost:8080/account/register when 
     * username does not exist in the system.
     * 
     * Expected Response:
     *  Status Code: 200
     *  Response Body: JSON representation of user object
     */
    @Test
    public void registerUserSuccessful() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"user\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assert.assertEquals(200, status);
        Account expectedAccount = new Account(2, "user", "password");
        Account actualAccount = objectMapper.readValue(response.body().toString(), Account.class);
        Assert.assertEquals(expectedAccount, actualAccount);

    }


     /**
     * Sending an HTTP POST request to localhost:8080/account/register when
     * username already exists in system
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void registerUserDuplicateUsername() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"user\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response1 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> response2 = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status1 = response1.statusCode();
        int status2 = response2.statusCode();
        Assert.assertEquals(200, status1);
        Assert.assertEquals(400, status2);
        Assert.assertEquals("", response2.body().toString());

    }

    /**
     * Sending an HTTP POST request to localhost:8080/account/register when 
     * no username is provided 
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void registerUserUsernameBlank() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/account/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"\", " +
                        "\"password\": \"password\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assert.assertEquals(400, status);
        Assert.assertEquals("", response.body().toString());

    }


    /**
     * Sending an HTTP POST request localhost:8080/account/register when
     * password is less than 4 characters
     * 
     * Expected Response:
     *  Status Code: 400
     *  Response Body: 
     */
    @Test
    public void registeUserPasswordLengthLessThanFour() throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/register"))
                .POST(HttpRequest.BodyPublishers.ofString("{" +
                        "\"username\": \"username\", " +
                        "\"password\": \"pas\" }"))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = webClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        Assert.assertEquals(400, status);
        Assert.assertEquals("", response.body().toString());

    }
}
