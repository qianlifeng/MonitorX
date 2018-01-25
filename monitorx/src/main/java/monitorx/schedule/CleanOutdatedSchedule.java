package monitorx.schedule;

import monitorx.domain.Node;
import monitorx.plugins.Status;
import monitorx.domain.Forewarning;
import monitorx.plugins.forewarning.ForewarningCheckPoint;
import monitorx.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CleanOutdatedSchedule {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    private static final Integer REMOVE_DATA_BEFORE_MINUTES = 10;

    @Scheduled(fixedRate = 1000 * 60)
    public void cleanCheckPoints() {
        AtomicLong before = new AtomicLong();
        nodeService.getNodes().forEach(o -> o.getForewarnings().forEach(j -> before.addAndGet(j.getCheckPoints().size())));
        for (Node node : nodeService.getNodes()) {
            for (Forewarning forewarning : node.getForewarnings()) {
                Iterator<ForewarningCheckPoint> iterator = forewarning.getCheckPoints().iterator();
                while (iterator.hasNext()) {
                    ForewarningCheckPoint checkPoint = iterator.next();

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -REMOVE_DATA_BEFORE_MINUTES);
                    if (checkPoint.getDatetime().before(calendar.getTime())) {
                        iterator.remove();
                    }
                }
            }
        }
        AtomicLong now = new AtomicLong();
        nodeService.getNodes().forEach(o -> o.getForewarnings().forEach(j -> now.addAndGet(j.getCheckPoints().size())));
        if (before.get() != now.get()) {
            logger.info("Deleting outdated checkpoint {} => {}", before.get(), now.get());
        }
    }

    @Scheduled(fixedRate = 1000 * 70)
    public void cleanStatusHistory() {
        AtomicLong before = new AtomicLong();
        nodeService.getNodes().forEach(o -> before.addAndGet(o.getStatusHistory().size()));
        for (Node node : nodeService.getNodes()) {
            Iterator<Status> iterator = node.getStatusHistory().iterator();
            while (iterator.hasNext()) {
                Status nodeStatus = iterator.next();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -REMOVE_DATA_BEFORE_MINUTES);
                if (nodeStatus.getLastUpdateDate().before(calendar.getTime())) {
                    iterator.remove();
                }
            }
        }
        AtomicLong now = new AtomicLong();
        nodeService.getNodes().forEach(o -> now.addAndGet(o.getStatusHistory().size()));
        if (before.get() != now.get()) {
            logger.info("Deleting outdated status {} => {}", before.get(), now.get());
        }
    }
}
