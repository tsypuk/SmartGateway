package ua.in.smartjava.domain.discovery;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import ua.in.smartjava.domain.device.Device;
import ua.in.smartjava.mongo.CrudRepository;
import ua.in.smartjava.upnp.DiscoveryService;

import static spark.Spark.after;
import static spark.Spark.post;

/**
 * Triggers start/stop of the discovery process
 */
public class DiscoveryController {

    public DiscoveryController(DiscoveryService discoveryService, CrudRepository<Device> deviceRepository) {
        Gson gson = new Gson();

        //TODO add parameter of seconds for discovery to run
        post("/api/discovery/", (request, response) -> {
            Discovery discovery = gson.fromJson(request.body(), Discovery.class);
            String action = discovery.getAction();
            if ("on".equalsIgnoreCase(action)) {
                discoveryService.startDiscoveryService(Integer.parseInt(discovery.getTime()), deviceRepository.findAll());
                response.status(200);
                HashMap<String, String> result = new HashMap<>();
                result.put("status", "Starting discovery");
                result.put("period", discovery.getTime());
                result.put("unit", TimeUnit.SECONDS.toString());
                return result;
            } else if ("off".equalsIgnoreCase(action)) {
                discoveryService.stopDiscoveryService();
                response.status(200);
                HashMap<String, String> result = new HashMap<>();
                result.put("status", "Discovery stopped");
                return result;
            }
            return "unknown command";
        }, gson::toJson);

        after((req, res) -> {
            res.type("application/json");
        });
    }

}
