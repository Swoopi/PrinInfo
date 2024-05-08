package com.cs336.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.cs336.dao.ItemDAO;

@WebListener
public class BackgroundJobManager implements ServletContextListener {
    private Thread taskThread;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ItemDAO itemDAO = new ItemDAO();
        BackgroundTask task = new BackgroundTask(itemDAO);
        taskThread = new Thread(task);
        taskThread.start();
        System.out.println("Background task started successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (taskThread != null && taskThread.isAlive()) {
            taskThread.interrupt();
            System.out.println("Background task stopped successfully.");
        }
    }
}
