package monitorx.domain.syncType;

public enum SyncTypeEnum {
    /**
     * MonitorX pull status data from node
     */
    PULL("pull", "Pull"),

    /**
     * Node push its status data to MonitorX
     */
    PUSH("push", "Push");


    String code;
    String name;

    SyncTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SyncTypeEnum getByCode(String code) {
        if ("pull".equals(code)) return PULL;
        if ("push".equals(code)) return PUSH;

        return null;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
