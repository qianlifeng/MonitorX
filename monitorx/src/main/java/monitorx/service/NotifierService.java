package monitorx.service;

import monitorx.domain.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifierService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConfigService configService;

    public List<Notifier> getNotifiers() {
        return configService.getConfig().getNotifiers();
    }

    public void addNotifier(Notifier notifier) {
        configService.getConfig().getNotifiers().add(notifier);
        configService.save();
    }

    public Notifier getNotifier(String id) {
        for (Notifier notifier : getNotifiers()) {
            if (notifier.getId().equals(id)) {
                return notifier;
            }
        }

        return null;
    }

    public void removeNotifier(String id) {
        Notifier notifier = getNotifier(id);
        if (notifier != null) {
            getNotifiers().remove(notifier);
            configService.save();
        }
    }
}