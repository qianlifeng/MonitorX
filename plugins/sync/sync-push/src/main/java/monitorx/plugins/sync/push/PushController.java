package monitorx.plugins.sync.push;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.plugins.IRequestDispatcher;
import monitorx.plugins.Status;
import org.apache.commons.io.IOUtils;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

/**
 * @author qianlifeng
 */
@Extension
public class PushController implements IRequestDispatcher {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public String getUrl() {
        return "/status/upload/";
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            InputStream inputStream = request.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer, "utf-8");
            String requestBody = writer.toString();
            JSONObject nodeStatusJSON = JSON.parseObject(requestBody);

            String nodeCode = nodeStatusJSON.getString("nodeCode");
            Status status = JSON.parseObject(JSON.toJSONString(nodeStatusJSON.getJSONObject("nodeStatus")), Status.class);
            status.setLastUpdateDate(new Date());
            logger.info("Upload status from ip:" + getIpAddr(request) + ", request body:" + requestBody);

            NodeStatus.update(nodeCode, status);

            response.getWriter().write("ok");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}