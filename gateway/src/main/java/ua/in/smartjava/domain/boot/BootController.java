package ua.in.smartjava.domain.boot;

import com.google.gson.Gson;

import static spark.Spark.after;
import static spark.Spark.post;

public class BootController {

    public BootController(BootService bootService) {
        //Boot REST APi
        Gson gson = new Gson();

        post("/api/boot/", (request, response) -> {
            BootAction bootAction = gson.fromJson(request.body(), BootAction.class);

            switch (bootAction.getAction()) {
                case "stop":
                    bootService.stopDeviceServers();
                    break;
                case "reload":
                    bootService.bootDeviceServers();
                    break;
            }
            return "ok";
        }, gson::toJson);

        after((req, res) -> {
            res.type("application/json");
        });
    }
}
