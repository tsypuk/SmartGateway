package ua.in.smartjava.domain.boot;

import com.google.gson.Gson;

import static spark.Spark.after;
import static spark.Spark.post;

public class BootController {

    public BootController(BootService bootService) {
        //Boot REST APi
        Gson gson = new Gson();

        post("/api/boot/:id", (req, res) -> {
            String action = req.params(":id");
            switch (action) {
                case "stop":
                    bootService.stopDeviceServers();
                    break;
                case "start":
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
