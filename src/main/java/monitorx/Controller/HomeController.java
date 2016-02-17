package monitorx.controller;

import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("nodes", nodeService.getNodes());
        return "index";
    }

    @RequestMapping("/n/")
    public String node() {
        return "node";
    }

    @RequestMapping("/n/new/")
    public String newNode() {
        return "nodeNew";
    }
}