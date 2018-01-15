package monitorx.schedule;

import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import monitorx.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

@Component
public class CleanOutdatedSchedule {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    @Scheduled(fixedRate = 1000 * 60)
    public void cleanCheckPoints() {
        for (Node node : nodeService.getNodes()) {
            for (Forewarning forewarning : node.getForewarnings()) {
                Iterator<ForewarningCheckPoint> iterator = forewarning.getFireRuleContext().getCheckPoints().iterator();
                while (iterator.hasNext()) {
                    ForewarningCheckPoint checkPoint = iterator.next();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.HOUR, -1);
                    if (checkPoint.getDatetime().before(calendar.getTime())) {
                        //remove checkpoints 2 days before
                        iterator.remove();
                        logger.info("Deleting outdated check-point: " + checkPoint.getDatetime());
                    }
                }
            }
        }
    }

    @Scheduled(fixedRate = 1000 * 70)
    public void cleanStatusHistory() {
        for (Node node : nodeService.getNodes()) {
            Iterator<NodeStatus> iterator = node.getStatusHistory().iterator();
            while (iterator.hasNext()) {
                NodeStatus nodeStatus = iterator.next();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR, -1);
                if (nodeStatus.getLastUpdateDate().before(calendar.getTime())) {
                    //remove checkpoints 2 days before
                    iterator.remove();
                    logger.info("Deleting outdated status: " + nodeStatus.getLastUpdateDate());
                }
            }
        }
    }
}
