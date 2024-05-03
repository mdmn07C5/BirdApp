package Controller;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Post;
import Service.AccountService;
import Service.PostService;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class BirdAppController {
    AccountService accountService;
    PostService postService;

    
    public BirdAppController() {
        this.accountService = new AccountService();
        this.postService = new PostService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/account/register", this::accountRegisterHandler);

        app.post("/account/login", this::accountLoginHandler);

        app.post("/posts", this::postCreateHandler);

        app.get("/posts", this::postsGetHandler);

        app.get("/posts/{post_id}", this::postsGetByIdHandler);

        app.delete("/posts/{post_id}", this::postsDeleteByIdHandler);
        
        app.patch("/posts/{post_id}", this::postsUpdateHandler);
        

        return app;
    }

    private void accountRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account newAcc = this.accountService.addAccount(acc);
        if (newAcc == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newAcc));
        }
    }

    private void accountLoginHandler(Context ctx) throws JsonProcessingException 
    {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account retrievedAcc = this.accountService.getAccount(acc);

        if (retrievedAcc ==  null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(retrievedAcc));
        }
    }

    private void postCreateHandler(Context ctx) throws JsonProcessingException {
         ObjectMapper mapper = new ObjectMapper();
        Post post = mapper.readValue(ctx.body(), Post.class);
        Post createdPost = this.postService.addPost(post);
        if (createdPost == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(createdPost));
        }
    }

    private void postsGetHandler(Context ctx) throws JsonProcessingException {
        ctx.json(postService.getPosts());
    }

    private void postsGetByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int post_id = Integer.parseInt(ctx.pathParam("post_id"));
        Post post = postService.getPostById(post_id);
        if (post == null) {
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(post));
        }
    }

    private void postsDeleteByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int post_id = Integer.parseInt(ctx.pathParam("post_id"));
        Post post = postService.deletePostById(post_id);
        if (post == null) {
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(post));
        }
    }

    private void postsUpdateHandler(Context ctx) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        int post_id = Integer.parseInt(ctx.pathParam("post_id"));
        Post partial = mapper.readValue(ctx.bodyAsBytes(), Post.class);

        Post newPost = postService.updatePost(post_id, partial.getPost_content());
        if (newPost == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newPost));
        }
    }
}

