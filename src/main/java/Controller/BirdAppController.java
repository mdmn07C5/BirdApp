package Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class BirdAppController {
    AccountService accountService;

    
    public BirdAppController() {
        this.accountService = new AccountService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/account/register", this::accountRegisterHandler);

        app.post("/account/login", this::accountLoginHandler);

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


}

