package monitorx.plugins.sync.push;

import monitorx.plugins.Status;

import java.util.*;

/**
 * @author qianlifeng
 */
public class NodeStatus {
    private static Map<String, Status> nodes = new HashMap<>();

    public static void update(String code, Status status) {
        nodes.put(code, status);
    }

    public static Status getLatestNodeStatus(String code, PushSyncConfig config) {
        Status status = nodes.get(code);
        if (status != null) {
            //check time - last push time < interval
            if (new Date().compareTo(addSecond(status.getLastUpdateDate(), config.getPushInterval())) < 0) {
                return status;
            }
        }

        return null;
    }

    private static Date addSecond(Date date, int seconds) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds);
        return cal.getTime();
    }
}