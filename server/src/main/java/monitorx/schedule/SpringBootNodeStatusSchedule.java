package monitorx.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Metric;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.syncType.SpringBootSyncTypeConfig;
import monitorx.domain.syncType.SyncTypeEnum;
import monitorx.service.NodeService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class SpringBootNodeStatusSchedule implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Node node : nodeService.getNodes()) {
            if (node.getSyncTypeEnum() == SyncTypeEnum.SPRING_BOOT) {
                SpringBootSyncTypeConfig config = ((SpringBootSyncTypeConfig) node.getSyncTypeConfig());
                if (config != null) {
                    taskRegistrar.addFixedRateTask(new SpringBootNodeStatusTask(node), config.getInterval() * 1000);
                }
            }
        }
    }

    private class SpringBootNodeStatusTask implements Runnable {

        Node node;

        public SpringBootNodeStatusTask(Node node) {
            this.node = node;
        }

        public void run() {
            try {
                logger.info("Sync Node Status: " + node.getTitle());
                SpringBootSyncTypeConfig config = ((SpringBootSyncTypeConfig) node.getSyncTypeConfig());


                NodeStatus nodeStatus = new NodeStatus();
                nodeStatus.setMetrics(new ArrayList<Metric>());
                nodeStatus.setLastUpdateDate(new Date());

                //up status
                String healthUrl = config.getUrl() + "/health";
                String response = Request.Get(healthUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                JSONObject healthJson = JSON.parseObject(response);
                boolean up = "UP".equals(healthJson.getString("status").toUpperCase());
                nodeStatus.setStatus(up ? "up" : "down");

                //health status
                Metric healthMetric = new Metric();
                healthMetric.setTitle("Health");
                healthMetric.setType("text");
                StringBuilder sb = new StringBuilder();
                JSONObject jms = healthJson.getJSONObject("jms");
                if (jms != null) {
                    boolean isUp = "UP".equals(jms.getString("status"));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-paper-plane-o'></i> JMS - " + jms.getString("provider")
                            + "<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                JSONObject disk = healthJson.getJSONObject("diskSpace");
                if (disk != null) {
                    boolean isUp = "UP".equals(disk.getString("status"));
                    long total = Math.round(disk.getLong("total") / (1024 * 1024 * 1024));
                    long free = Math.round(disk.getLong("free") / (1024 * 1024 * 1024));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-tasks'></i>  DISK - Total: " + total + "G, Free: " + free
                            + "G<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                JSONObject db = healthJson.getJSONObject("db");
                if (db != null) {
                    boolean isUp = "UP".equals(db.getString("status"));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-database'></i> DB - " + db.getString("database")
                            + "<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                healthMetric.setValue(sb.toString());
                healthMetric.setContext(response);
                nodeStatus.getMetrics().add(healthMetric);

                //info metric
                String infoUrl = config.getUrl() + "/info";
                response = Request.Get(infoUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                JSONObject infoJson = JSON.parseObject(response);
                Metric infoMetric = new Metric();
                infoMetric.setTitle("Info");
                infoMetric.setType("text");
                String info = JSON.toJSONString(infoJson, true);
                infoMetric.setValue(info.replace("\n", "<br/>").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
                infoMetric.setContext(info);
                nodeStatus.getMetrics().add(infoMetric);

                //mem metric
                String metricsUrl = config.getUrl() + "/metrics";
                response = Request.Get(metricsUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                JSONObject metricsJson = JSON.parseObject(response);
                Metric memoryMetric = new Metric();
                memoryMetric.setTitle("Used Memory(Mb)");
                memoryMetric.setType("line");
                memoryMetric.setWidth(1.0);
                long usedMemory = Math.round((metricsJson.getLong("heap.used") + metricsJson.getLong("nonheap.used")) / 1024);
                long initHeapMemory = Math.round((metricsJson.getLong("heap.init")) / 1024);
                long maxHeapMemory = Math.round((metricsJson.getLong("heap")) / 1024);
                long committedMemory = Math.round((metricsJson.getLong("heap.committed") + metricsJson.getLong("nonheap.committed")) / 1024);
                JSONObject val = new JSONObject();
                val.put("x", DateFormatUtils.format(new Date(), "HH:mm"));
                val.put("y", usedMemory);
                val.put("xcount", 10);
                memoryMetric.setValue(JSON.toJSONString(val));
                nodeStatus.getMetrics().add(memoryMetric);

                Metric memoryDetailMetric = new Metric();
                memoryDetailMetric.setTitle("Memory");
                memoryDetailMetric.setType("text");
                StringBuilder memoryDetailBuilder = new StringBuilder();
                //memroy
                memoryDetailBuilder.append("<div style='margin-bottom:5px;'><b>Used Memory</b> <span class='pull-right'>" + usedMemory + "M / " + committedMemory + "M</span></div>");
                long memroyPercent = Math.round(((usedMemory * 1.0) / committedMemory) * 100);
                memoryDetailBuilder.append("<div class='progress'><div class='progress-bar progress-bar-striped' style='width: " + memroyPercent + "%;'>" + memroyPercent + "%</div></div>");
                //heap memory
                memoryDetailBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Initial Heap Memory (-Xms)</b> <span class='pull-right'>" + (initHeapMemory) + "M</span></div>");
                memoryDetailBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Maximum Heap Memory (-Xmx)</b> <span class='pull-right'>" + (maxHeapMemory) + "M</span></div>");

                memoryDetailMetric.setValue(memoryDetailBuilder.toString());
                memoryDetailMetric.setContext(response);
                nodeStatus.getMetrics().add(memoryDetailMetric);

                //JVM metric
                Metric jvmMetric = new Metric();
                jvmMetric.setTitle("JVM");
                jvmMetric.setType("text");
                StringBuilder jvmBuilder = new StringBuilder();
                //uptime
                String uptimeHour = String.format("%.2f", (metricsJson.getLong("uptime")) / 3600000f);
                jvmBuilder.append("<div style='margin-bottom:5px;'><b>Uptime</b> <span class='pull-right'>" + uptimeHour + " H</span></div>");
                //systemload
                String systemLoad = metricsJson.getString("systemload.average");
                jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>SystemLoad (last minute)</b> <span class='pull-right'>" + systemLoad + "</span></div>");
                //processors
                String processors = metricsJson.getString("processors");
                jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Processors</b> <span class='pull-right'>" + processors + "</span></div>");
                //classes
                Long currentLoadClasses = metricsJson.getLong("classes");
                Long totalLoadClasses = metricsJson.getLong("classes.loaded");
                Long unloadedClasses = metricsJson.getLong("classes.unloaded");
                jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Classes</b> <span class='pull-right'>Total: " + totalLoadClasses + "</span></div>");
                jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Current: " + currentLoadClasses + "</div>");
                jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Unloaded: " + unloadedClasses + "</div>");
                //thread
                Long currentThreads = metricsJson.getLong("threads");
                Long daemonThreads = metricsJson.getLong("threads.daemon");
                Long totalStartedThreads = metricsJson.getLong("threads.totalStarted");
                jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Threads</b> <span class='pull-right'>Total Started: " + totalStartedThreads + "</span></div>");
                jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Current: " + currentThreads + "</div>");
                jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Daemon: " + daemonThreads + "</div>");

                jvmMetric.setValue(jvmBuilder.toString());
                nodeStatus.getMetrics().add(jvmMetric);

                node.setStatus(nodeStatus);
                node.getStatusHistory().add(nodeStatus);

                nodeService.addCheckPoints(node);
            } catch (IOException e) {
                logger.error("Sync Node failed:" + node.getTitle(), e);
                NodeStatus nodeStatus = new NodeStatus();
                nodeStatus.setStatus("down");
                nodeStatus.setLastUpdateDate(new Date());
                node.setStatus(nodeStatus);
            }
        }
    }
}