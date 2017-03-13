package monitorx.controller;

import monitorx.domain.Node;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

@Controller
public class HomeController {

    @Autowired
    NodeService nodeService;
    @Value("classpath:/ui/index.html")
    private Resource indexHtml;

    @RequestMapping("/")
    public Object index() {
        return ResponseEntity.ok().body(indexHtml);
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
        model.addAttribute("context", escapeHtml4(getForewarningContext(node, metric)));
        return "forewarning";
    }

    @RequestMapping("/forewarning/edit/")
    public String editFirewarning(HttpServletRequest request, Model model) {
        String node = request.getParameter("node");
        String metric = request.getParameter("metric");
        model.addAttribute("context", escapeHtml4(getForewarningContext(node, metric)));
        return "forewarning";
    }

    private String getForewarningContext(String nodeCode, String metric) {
        Node node = nodeService.getNode(nodeCode);
        return nodeService.getNodeMetricContext(node, metric);
    }
}