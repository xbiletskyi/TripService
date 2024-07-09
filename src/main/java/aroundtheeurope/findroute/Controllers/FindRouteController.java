package aroundtheeurope.findroute.Controllers;


import aroundtheeurope.findroute.Services.FindRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FindRouteController {
    @Autowired
    private final FindRouteService findRouteService;

    @Autowired
    public FindRouteController(FindRouteService findRouteService) {
        this.findRouteService = findRouteService;
    }

    @GetMapping("/findroute")
    public String findRoute(@RequestParam("origin") String origin, @RequestParam("destination") String destination,
                            @RequestParam("budget") String budget) {
        return "";
    }
}
