package com.cs336.servlet;

import com.cs336.dao.ItemDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet("/ViewItemsServlet")
public class ViewItemsServlet extends HttpServlet {
    private ExecutorService executor;

    @Override
    public void init() throws ServletException {
        super.init();
        ItemDAO itemDAO = new ItemDAO();
        executor = Executors.newSingleThreadExecutor();
        executor.submit(new BackgroundTask(itemDAO));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ItemDAO itemDAO = new ItemDAO();
        request.setAttribute("items", itemDAO.getActiveItems());
        RequestDispatcher dispatcher = request.getRequestDispatcher("viewItems.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    public void destroy() {
        executor.shutdownNow();
        super.destroy();
    }

    private static class BackgroundTask implements Runnable {
        private ItemDAO itemDAO;

        public BackgroundTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    itemDAO.checkAndCloseBids();
                    Thread.sleep(60000); // Check every minute
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
