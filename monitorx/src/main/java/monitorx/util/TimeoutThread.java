package monitorx.util;

import monitorx.domain.Node;

/**
 * @author qianlifeng
 */
public class TimeoutThread {
    Thread interrupter;
    Thread target;
    Node node;
    long timeout;

    /**
     * @param target  The Runnable target to be executed
     * @param timeout The time in milliseconds before target will be interrupted or stopped
     */
    public TimeoutThread(Node node, Runnable target, long timeout) {
        this.node = node;
        this.timeout = timeout;
        this.target = new Thread(target);
        this.interrupter = new Thread(new Interrupter());
    }

    public void execute() {
        new Thread(() -> {
            // Start target and interrupter
            target.start();
            interrupter.start();

            // Wait for target to finish or be interrupted by interrupter
            try {
                target.join();
                interrupter.interrupt();
            } catch (InterruptedException e) {
            }
        }).start();
    }

    private class Interrupter implements Runnable {

        Interrupter() {
        }

        @Override
        public void run() {
            try {
                // Wait for timeout period and then kill this target
                Thread.sleep(timeout);
                target.interrupt();
            } catch (InterruptedException e) {
            }
        }
    }
}
