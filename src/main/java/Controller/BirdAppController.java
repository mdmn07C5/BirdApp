package Controller;

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


}

