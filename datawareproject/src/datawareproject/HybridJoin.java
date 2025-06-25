package datawareproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class HybridJoin implements Runnable {
    private final DataProcessor dataProcessor;
    private final Connection conn;
    private final BlockingQueue<Transaction> queue;
    private final Deque<Long> joinQueue; 
    private static final int MD_SEGMENT_SIZE = 10;
    private final StreamGenerator streamGenerator; 

    public HybridJoin(DataProcessor dataProcessor, Connection conn, BlockingQueue<Transaction> queue, StreamGenerator streamGenerator) {
        this.dataProcessor = dataProcessor;
        this.conn = conn;
        this.queue = queue;
        this.joinQueue = new LinkedList<>();
        this.streamGenerator = streamGenerator;
    }

    @Override
    public void run() {
        try {
            System.out.println("HybridJoin thread started.");
            while (!Thread.currentThread().isInterrupted() && !isProcessingComplete()) {
                if (!queue.isEmpty()) {
                    loadTransactions();
                    int processedCount = loadAndProcessMD();
                    if (processedCount > 0) {
                        afterMDProcessing();
                    }
                    streamGenerator.adjustChunkSize(processedCount);
                } else if (streamGenerator.isCompleted()) {
                    break; 
                } else {
                    Thread.sleep(3000);
                }
            }
        } catch (InterruptedException | SQLException e) {
            System.out.println("HybridJoin thread interrupted or encountered an SQLException.");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
        	 System.out.println("HybridJoin thread terminating.");
        }
    }


    private void loadTransactions() throws InterruptedException {
        Transaction transaction = queue.take();
        long productId = transaction.getProductId();
        dataProcessor.addTransaction(transaction);
        if (!joinQueue.contains(productId)) {
            joinQueue.offer(productId);
           // System.out.println("Added product ID to joinQueue: " + productId);
        }
    }

    private int loadAndProcessMD() throws SQLException {
        int processedCount = 0;
        if (joinQueue.size() >= MD_SEGMENT_SIZE) {
            List<Long> segmentIds = new LinkedList<>();
            //System.out.println("Loading MD for segment size: " + MD_SEGMENT_SIZE);

            for (int i = 0; i < MD_SEGMENT_SIZE; i++) {
                Long productId = joinQueue.poll();
                segmentIds.add(productId);
                //System.out.println("Polled product ID from joinQueue: " + productId);
            }

            dataProcessor.loadSegmentIntoDiskBuffer(conn, segmentIds);
            processedCount = processAndJoinData(segmentIds);
        }
        return processedCount;
    }
    
    private void afterMDProcessing() {
        try {
            dataProcessor.processAndJoin(conn); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int processAndJoinData(List<Long> segmentIds) throws SQLException {
        int processedCount = 0;
        for (Long productId : segmentIds) {
            MasterData md = dataProcessor.getMasterData(productId);
            if (md != null) {
                List<Transaction> transactions = dataProcessor.getTransactions(productId);
                if (transactions != null) {
                    for (Transaction transaction : transactions) {
                        processJoinedData(transaction, md);
                    }
                    processedCount += transactions.size();
                  //  System.out.println("Processed " + transactions.size() + " transactions for product ID: " + productId);
                }
            }
        }
       // System.out.println("Total processed count in this batch: " + processedCount);
        return processedCount;
    }
    
    private boolean isProcessingComplete() {
        return streamGenerator.isCompleted() && queue.isEmpty();
    }

    private void processJoinedData(Transaction transaction, MasterData masterData) {
       
    }
}
