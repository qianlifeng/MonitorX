package monitorx.controller;

import monitorx.domain.Node;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping("/notifier/")
    public String notifier() {
        return "notifier";
    }

    @RequestMapping("/node/")
    public String node() {
        return "node";
    }

    @RequestMapping("/node/new/")
    public String newNode() {
        return "nodeNew";
    }

    @RequestMapping("/notifier/new/")
    public String newNotifier() {
        return "notifierNew";
    }

    @RequestMapping("/forewarning/new/")
    public String newFirewarning(HttpServletRequest request, Model model) {
        String node = request.getParameter("node");
        String metric = request.getParameter("metric");
        model.addAttribute("context", getForewarningContext(node, metric));
        return "forewarningNew";
    }

    @RequestMapping("/forewarning/edit/")
    public String editFirewarning(HttpServletRequest request, Model model) {
        String node = request.getParameter("node");
        String metric = request.getParameter("metric");
        model.addAttribute("context", getForewarningContext(node, metric));
        return "forewarningNew";
    }

    private String getForewarningContext(String nodeCode, String metric) {
        Node node = nodeService.getNode(nodeCode);
        return nodeService.getNodeMetricContext(node, metric);
    }
}