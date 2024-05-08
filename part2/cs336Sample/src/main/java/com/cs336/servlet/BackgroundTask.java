package com.cs336.servlet;
import com.cs336.dao.ItemDAO;



public class BackgroundTask implements Runnable {
    private ItemDAO itemDAO;

    public BackgroundTask(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                itemDAO.checkAndCloseBids();
                System.out.println("Updating Active Items");
                Thread.sleep(20000); // Check every minute
            }
        } catch (InterruptedException e) {
            System.out.println("Background task interrupted");
            Thread.currentThread().interrupt();
        }
    }
}

