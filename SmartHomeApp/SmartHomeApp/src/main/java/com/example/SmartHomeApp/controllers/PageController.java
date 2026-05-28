package com.example.SmartHomeApp.controllers;

import com.example.SmartHomeApp.entities.Measurement;
import com.example.SmartHomeApp.repositories.StateRepository;
import com.example.SmartHomeApp.repositories.UserRepository;
import com.example.SmartHomeApp.services.MeasurementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    private final MeasurementService measurementService;
    private final UserRepository userRepository;
    private final StateRepository stateRepository;

    public PageController(MeasurementService measurementService,
                          UserRepository userRepository,
                          StateRepository stateRepository) {
        this.measurementService = measurementService;
        this.userRepository = userRepository;
        this.stateRepository = stateRepository;
    }

    @GetMapping("/session")
    public String getSessionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getId();
    }

    @GetMapping("/user/main")
    public String getMain(Model model) {

        List<Measurement> list = measurementService.getAllMeasurements();

        List<Measurement> cleanList = list.stream()
                .filter(m -> m.getMeasurementTime() != null)
                .toList();

        model.addAttribute("times",
                cleanList.stream()
                        .map(m -> m.getMeasurementTime().toString())
                        .toList()
        );

        model.addAttribute("temperatures",
                cleanList.stream()
                        .map(Measurement::getTemperature)
                        .toList()
        );

        model.addAttribute("humidities",
                cleanList.stream()
                        .map(Measurement::getHumidity)
                        .toList()
        );

        return "main";
    }

    @GetMapping("/{path:^(?!js|css|img).*}/**")
    public String catchAll() {
        return "redirect:/user/main";
    }

    @GetMapping("")
    public String catchAllPage() {
        return "redirect:/user/main";
    }
}
