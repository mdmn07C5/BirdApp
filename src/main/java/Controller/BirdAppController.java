package Controller;

import io.javalin.Javalin;

public class BirdAppController {
    
    public BirdAppController() {

    }

    public Javalin startAPI() {

        return Javalin.create();
    }
}
