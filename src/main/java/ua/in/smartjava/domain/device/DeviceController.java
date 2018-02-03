package ua.in.smartjava.domain.device;

import com.google.gson.Gson;

import ua.in.smartjava.mongo.CrudRepository;

import static spark.Spark.after;
import static spark.Spark.get;

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

        after((req, res) -> {
            res.type("application/json");
        });
    }


}
