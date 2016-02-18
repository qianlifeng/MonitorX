package monitorx.domain.notifier;

public interface ISender {
    void send(String title, String msg);
}
