package monitorx.plugins;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data contract exchanged between application server and MonitorX
 */
public class Status {
    /**
     * up/down/unkown
     */
    String status = "unknown";

    List<Metric> metrics;

    Date lastUpdateDate;

    public String getFormattedLastUpdateDate() {
        if (lastUpdateDate == null) {
            return "";
        }

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(lastUpdateDate);
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public String toString() {
        return status;
    }

    public static Status down() {
        Status status = new Status();
        status.setLastUpdateDate(new Date());
        status.setStatus("down");
        status.setMetrics(new ArrayList<>());
        return status;
    }
}