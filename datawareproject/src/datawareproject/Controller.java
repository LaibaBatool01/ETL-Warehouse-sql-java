package datawareproject;

public class Controller implements Runnable {
    private final StreamGenerator streamGenerator;
    private final HybridJoin hybridJoin;
    private volatile boolean running = true;

    public Controller(StreamGenerator streamGenerator, HybridJoin hybridJoin) {
        this.streamGenerator = streamGenerator;
        this.hybridJoin = hybridJoin;
    }

    @Override
    public void run() {
        try {
            while (running) {
                monitorAndAdjust();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void monitorAndAdjust() {
       
        int adjustedChunkSize = calculateAdjustedChunkSize();
        streamGenerator.adjustChunkSize(adjustedChunkSize);
    }

    private int calculateAdjustedChunkSize() {
       
        return 1000; 
    }

    public void stop() {
        running = false;
    }
}
