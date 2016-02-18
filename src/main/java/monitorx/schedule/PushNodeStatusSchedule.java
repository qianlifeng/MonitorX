package monitorx.schedule;

import monitorx.domain.Node;
import monitorx.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Check if push type node is still pushing status data
 */
@Component
public class PushNodeStatusSchedule {

    @Autowired
    NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(fixedRate = 1000 * 20)
    public void checkPushStatus() {
        for (Node node : nodeService.getNodes()) {
            if (node.getSyncType().equals("push") && node.getStatus() != null && node.getStatus().getLastUpdateDate() != null) {
                long seconds = (new Date().getTime() - node.getStatus().getLastUpdateDate().getTime()) / 1000;
                if (seconds > 30) {
                    node.getStatus().setStatus("down");
                    node.getStatus().setLastUpdateDate(new Date());
                    logger.warn("Node is down:" + node.getCode());
                }
            }
        }
    }
}
