package ua.in.smartjava.domain.device;

import com.google.gson.Gson;

import ua.in.smartjava.mongo.CrudRepository;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class DeviceController {

    public DeviceController(CrudRepository<Device> deviceRepository) {
        //Boot REST APi
        Gson gson = new Gson();

        get("/api/devices", (request, response) -> {
            return deviceRepository.findAll();
        }, gson::toJson);

        get("/api/devices/:id", (req, res) -> {
            String id = req.params(":id");
            Device device = deviceRepository.findById(id);
            if (device != null) {
                return device;
            }
            res.status(400);
            return "no user";
//            return new ResponseError("No user with id '%s' found", id);
        }, gson::toJson);

        put("/api/devices/:id", (req, res) -> {
            String id = req.params(":id");
            Device device = gson.fromJson(req.body(), Device.class);
            deviceRepository.update(device, id);
            res.status(204);
            return "done";

        });

        post("/api/devices/", (req, res) -> {
            Device device = gson.fromJson(req.body(), Device.class);
            deviceRepository.save(device);
            res.status(204);
            return "created";
        });

        after((req, res) -> {
            res.type("application/json");
        });
    }


}
