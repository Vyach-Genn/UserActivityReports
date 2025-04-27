package prosky.user_activity_reports.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prosky.user_activity_reports.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/last-hour")
    public String usersLastHour(Model model) {
        model.addAttribute("data", reportService.getUsersAndFormsLastHour());
        return "Last-hour";
    }

    @GetMapping("/incomplete-forms")
    public String incompleteForms(Model model) {
        model.addAttribute("data", reportService.getFromSteps());
        return "Incomplete-froms";
    }

    @GetMapping("/top-froms")
    public String topFroms(Model model) {
        model.addAttribute("data", reportService.getTop5Froms());
        return "Top-froms";
    }
}
