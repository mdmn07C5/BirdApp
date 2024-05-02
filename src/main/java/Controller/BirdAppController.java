package Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;


public class BirdAppController {
    
    public BirdAppController() {

    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/account/register", this::accountRegisterHandler);

        return app;
    }

    private void accountRegisterHandler(Context ctx){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ctx.status(200).json(mapper.writeValueAsString(null));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

