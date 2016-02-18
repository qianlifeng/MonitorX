package monitorx.controller;

import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/notifier/")
    public String notifier() {
        return "notifier";
    }

    @RequestMapping("/n/")
    public String node() {
        return "node";
    }

    @RequestMapping("/n/new/")
    public String newNode() {
        return "nodeNew";
    }

    @RequestMapping("/notifier/new/")
    public String newNotifier() {
        return "notifierNew";
    }
}