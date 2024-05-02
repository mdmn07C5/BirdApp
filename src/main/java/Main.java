import Controller.BirdAppController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Hello world!");
        BirdAppController controller = new BirdAppController();
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}