package monitorx.plugins.sync.springboot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.plugins.Metric;
import monitorx.plugins.Status;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.ISyncConfig;
import monitorx.plugins.sync.SyncContext;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.client.fluent.Request;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Extension
public class SpringBootSync implements ISync {

    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, Long> lastHttpStatus = new HashMap<>();

    @Override
    public String getCode() {
        return "sync-springboot";
    }

    @Override
    public String getName() {
        return "SpringBoot";
    }

    @Override
    public String getDescription() {
        return "Please Enable Spring Actuator in your Spring Boot Application";
    }

    @Override
    public Status sync(SyncContext syncContext) {
        try {
            SpringBootSyncConfig config = (SpringBootSyncConfig) syncContext.getSyncConfig();

            Status nodeStatus = new Status();
            nodeStatus.setMetrics(new ArrayList<Metric>());
            nodeStatus.setLastUpdateDate(new Date());

            //health status
            String healthUrl = config.getUrl() + "/health";
            String response = Request.Get(healthUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
            JSONObject healthJson = JSON.parseObject(response);
            boolean up = "UP".equals(healthJson.getString("status").toUpperCase());
            nodeStatus.setStatus(up ? "up" : "down");
            nodeStatus.getMetrics().add(getHealthData(healthJson));

            //info metric
            String infoUrl = config.getUrl() + "/info";
            response = Request.Get(infoUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
            JSONObject infoJson = JSON.parseObject(response);
            nodeStatus.getMetrics().add(getInfoData(infoJson));

            //Memory metric
            String metricsUrl = config.getUrl() + "/metrics";
            response = Request.Get(metricsUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
            JSONObject metricsJson = JSON.parseObject(response);

            //http status counter
            nodeStatus.getMetrics().add(getHttpRequestData(metricsJson));

            nodeStatus.getMetrics().add(getMemoryLineData(metricsJson));
            nodeStatus.getMetrics().add(getMemoryDetailData(metricsJson));

            //GC metric
            Metric gcMetric = getGCData(metricsJson);
            if (gcMetric != null) {
                nodeStatus.getMetrics().add(gcMetric);
            }

            //JVM metric
            nodeStatus.getMetrics().add(getJVMData(metricsJson));

            //link
            nodeStatus.getMetrics().add(getLinkData(config.getUrl()));
            return nodeStatus;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return Status.down();
    }


    private Metric getHealthData(JSONObject healthJson) {
        Metric healthMetric = new Metric();
        healthMetric.setTitle("Health");
        healthMetric.setType("text");
        StringBuilder sb = new StringBuilder();
        JSONObject jms = healthJson.getJSONObject("jms");
        JSONObject context = new JSONObject();
        if (jms != null) {
            boolean isJmsUp = "UP".equals(jms.getString("status"));
            context.put("jsm", isJmsUp);
            sb.append("<div class='alert alert-" + (isJmsUp ? "success" : "danger") + "'><i class='fa fa-paper-plane-o'></i> JMS - " + jms.getString("provider")
                    + "<span class='pull-right'>" + jms.getString("status") + "</span></div>");
        }
        JSONObject disk = healthJson.getJSONObject("diskSpace");
        if (disk != null) {
            boolean isDiskUp = "UP".equals(disk.getString("status"));
            context.put("disk", isDiskUp);
            long total = Math.round(disk.getLong("total") / (1024 * 1024 * 1024));
            long free = Math.round(disk.getLong("free") / (1024 * 1024 * 1024));
            sb.append("<div class='alert alert-" + (isDiskUp ? "success" : "danger") + "'><i class='fa fa-tasks'></i>  DISK - Total: " + total + "G, Free: " + free
                    + "G<span class='pull-right'>" + disk.getString("status") + "</span></div>");
        }
        JSONObject db = healthJson.getJSONObject("db");
        if (db != null) {
            boolean isDBUp = "UP".equals(db.getString("status"));
            context.put("db", isDBUp);
            sb.append("<div class='alert alert-" + (isDBUp ? "success" : "danger") + "'><i class='fa fa-database'></i> DB - " + db.getString("database")
                    + "<span class='pull-right'>" + db.getString("status") + "</span></div>");
        }
        healthMetric.setValue(sb.toString());
        healthMetric.setContext(JSON.toJSONString(context));
        return healthMetric;
    }

    private Metric getInfoData(JSONObject infoJson) throws IOException {
        Metric infoMetric = new Metric();
        infoMetric.setTitle("Info");
        infoMetric.setType("text");
        String info = JSON.toJSONString(infoJson, true);
        infoMetric.setValue(info.replace("\n", "<br/>").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
        infoMetric.setContext(info);

        return infoMetric;
    }

    private Metric getMemoryLineData(JSONObject metricsJson) {
        long usedMemory = Math.round((metricsJson.getLong("heap.used") + metricsJson.getLong("nonheap.used")) / 1024);
        Metric memoryMetric = new Metric();
        memoryMetric.setTitle("Used Memory(Mb)");
        memoryMetric.setType("line");
        memoryMetric.setWidth(1.0);
        JSONObject val = new JSONObject();
        val.put("x", DateFormatUtils.format(new Date(), "HH:mm"));
        val.put("y", usedMemory);
        val.put("xcount", 10);
        memoryMetric.setValue(JSON.toJSONString(val));
        memoryMetric.setContext(JSON.toJSONString(val));
        return memoryMetric;
    }

    private Metric getMemoryDetailData(JSONObject metricsJson) {
        long usedMemory = Math.round((metricsJson.getLong("heap.used") + metricsJson.getLong("nonheap.used")) / 1024);
        long initHeapMemory = Math.round((metricsJson.getLong("heap.init")) / 1024);
        long maxHeapMemory = Math.round((metricsJson.getLong("heap")) / 1024);
        long committedMemory = Math.round((metricsJson.getLong("heap.committed") + metricsJson.getLong("nonheap.committed")) / 1024);

        Metric memoryDetailMetric = new Metric();
        memoryDetailMetric.setTitle("Memory");
        memoryDetailMetric.setType("text");
        StringBuilder memoryDetailBuilder = new StringBuilder();
        //memroy
        memoryDetailBuilder.append("<div style='margin-bottom:5px;'><b>Used Memory</b> <span class='pull-right'>" + usedMemory + "M / " + committedMemory + "M</span></div>");
        long memroyPercent = Math.round(((usedMemory * 1.0) / committedMemory) * 100);
        memoryDetailBuilder.append("<div class='progress active'><div class='progress-bar progress-bar-striped' style='width: " + memroyPercent + "%;'>" + memroyPercent + "%</div></div>");
        //heap memory
        memoryDetailBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Initial Heap Memory (-Xms)</b> <span class='pull-right'>" + (initHeapMemory) + "M</span></div>");
        memoryDetailBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Maximum Heap Memory (-Xmx)</b> <span class='pull-right'>" + (maxHeapMemory) + "M</span></div>");

        memoryDetailMetric.setValue(memoryDetailBuilder.toString());
        JSONObject context = new JSONObject();
        context.put("usedMemory", usedMemory);
        context.put("initHeapMemory", initHeapMemory);
        context.put("maxHeapMemory", maxHeapMemory);
        context.put("committedMemory", committedMemory);
        memoryDetailMetric.setContext(JSON.toJSONString(context));

        return memoryDetailMetric;
    }

    private Metric getGCData(JSONObject metricsJson) {
        Metric gcMetric = new Metric();
        gcMetric.setTitle("GC");
        gcMetric.setType("text");
        StringBuilder gcBuilder = new StringBuilder();
        Long gcCount = metricsJson.getLong("gc.ps_scavenge.count");
        if (gcCount == null) {
            return null;
        }

        String gcTime = String.format("%.2f", (metricsJson.getLong("gc.ps_scavenge.time")) / (gcCount * 1.0f));
        gcBuilder.append("<div style='margin-bottom:5px;'><b>Average Time</b> <span class='pull-right'>" + gcTime + " ms</span></div>");
        gcBuilder.append("<hr /><div style='margin-bottom:5px;'><b>GC Count</b> <span class='pull-right'>" + gcCount + "</span></div>");
        gcMetric.setValue(gcBuilder.toString());

        JSONObject context = new JSONObject();
        context.put("gcCount", gcCount);
        context.put("gcTime", gcTime);
        gcMetric.setContext(JSON.toJSONString(context));

        return gcMetric;
    }

    private Metric getJVMData(JSONObject metricsJson) {
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
        jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Classes</b> <span class='pull-right'>Total: <span class='badge'>" + totalLoadClasses + "</span></span></div>");
        jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Current: <span class='badge'>" + currentLoadClasses + "</span></div>");
        jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Unloaded: <span class='badge'>" + unloadedClasses + "</span></div>");
        //thread
        Long currentThreads = metricsJson.getLong("threads");
        Long daemonThreads = metricsJson.getLong("threads.daemon");
        Long totalStartedThreads = metricsJson.getLong("threads.totalStarted");
        jvmBuilder.append("<hr/><div style='margin-bottom:5px;'><b>Threads</b> <span class='pull-right'>Total Started:  <span class='badge'>" + totalStartedThreads + "</span></span></div>");
        jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Current: <span class='badge'>" + currentThreads + "</span></div>");
        jvmBuilder.append("<div style='margin-bottom:5px;text-align:right;'> Daemon: <span class='badge'>" + daemonThreads + "</span></div>");

        jvmMetric.setValue(jvmBuilder.toString());

        JSONObject context = new JSONObject();
        context.put("upHour", uptimeHour);
        context.put("systemLoad", systemLoad);
        context.put("processors", processors);
        context.put("currentLoadClasses", currentLoadClasses);
        context.put("totalLoadClasses", totalLoadClasses);
        context.put("unloadedClasses", unloadedClasses);
        context.put("currentThreads", currentThreads);
        context.put("daemonThreads", daemonThreads);
        context.put("totalStartedThreads", totalStartedThreads);
        jvmMetric.setContext(JSON.toJSONString(context));

        return jvmMetric;
    }

    private Metric getLinkData(String basicUrl) {
        Metric gcMetric = new Metric();
        gcMetric.setTitle("Links");
        gcMetric.setType("text");
        StringBuilder linkBuilder = new StringBuilder();
        linkBuilder.append("<div style='margin-bottom:5px;'>Configs<a href='" + basicUrl + "/autoconfig' class='pull-right'>" + basicUrl + "/autoconfig</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Beans<a href='" + basicUrl + "/beans' class='pull-right'>" + basicUrl + "/beans</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Props<a href='" + basicUrl + "/configprops' class='pull-right'>" + basicUrl + "/configprops</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Env<a href='" + basicUrl + "/env' class='pull-right'>" + basicUrl + "/env</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Mapping<a href='" + basicUrl + "/mappings' class='pull-right'>" + basicUrl + "/mappings</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Info<a href='" + basicUrl + "/info' class='pull-right'>" + basicUrl + "/info</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Metrics<a href='" + basicUrl + "/metrics' class='pull-right'>" + basicUrl + "/metrics</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>health<a href='" + basicUrl + "/health' class='pull-right'>" + basicUrl + "/health</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Thread Dump<a href='" + basicUrl + "/dump' class='pull-right'>" + basicUrl + "/dump</a></div>");
        linkBuilder.append("<hr/><div style='margin-bottom:5px;'>Http Trace<a href='" + basicUrl + "/trace' class='pull-right'>" + basicUrl + "/trace</a></div>");
        gcMetric.setValue(linkBuilder.toString());

        return gcMetric;
    }

    private Metric getHttpRequestData(JSONObject metricsJson) {
        Metric httpMetric = new Metric();
        httpMetric.setTitle("Http Request Per Second");
        httpMetric.setType("line");
        httpMetric.setWidth(1.0);

        Map<String, Long> status = new HashMap<>();
        for (Map.Entry<String, Object> items : metricsJson.entrySet()) {
            if (items.getKey().startsWith("counter.status")) {
                String statusCode = items.getKey().substring("counter.status.".length(), "counter.status.".length() + 3);
                if (!status.containsKey(statusCode)) {
                    status.put(statusCode, 0L);
                }

                status.put(statusCode, status.get(statusCode) + Long.parseLong(items.getValue().toString()));
            }
        }

        Map<String, Long> diff = new HashMap<>();
        for (String key : status.keySet()) {
            Long diffCounter = 0L;
            if (lastHttpStatus.containsKey(key)) {
                diffCounter = (status.get(key) - lastHttpStatus.get(key)) / 3;
                if (diffCounter < 0) {
                    diffCounter = status.get(key);
                }
            }
            diff.put(key, diffCounter);
        }

        JSONObject httpJson = new JSONObject();
        httpJson.put("x", DateFormatUtils.format(new Date(), "HH:mm:ss"));
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Long> item : diff.entrySet()) {
            JSONObject y = new JSONObject();
            y.put("name", item.getKey());
            y.put("value", item.getValue());
            jsonArray.add(y);
        }
        httpJson.put("y", jsonArray);
        httpJson.put("xcount", 20);
        httpJson.put("xinterval", 5);
        httpMetric.setValue(JSON.toJSONString(httpJson));

        JSONObject httpContext = new JSONObject();
        httpContext.put("current", diff);
        httpContext.put("history", jsonArray);
        httpMetric.setContext(JSON.toJSONString(httpContext));

        lastHttpStatus = status;

        return httpMetric;
    }

    @Override
    public ISyncConfig getSyncConfig() {
        return new SpringBootSyncConfig();
    }
}