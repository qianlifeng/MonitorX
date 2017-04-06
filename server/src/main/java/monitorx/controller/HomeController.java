package monitorx.controller;

import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}