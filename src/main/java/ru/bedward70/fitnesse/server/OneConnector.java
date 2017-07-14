package ru.bedward70.fitnesse.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;

/**
 * Created by ebalovnev on 13.07.17.
 */
public class OneConnector {

    public static void main( String[] args ) throws Exception
    {
        // The Server
        Server server = new Server(new ExecutorThreadPool());

        // HTTP connector
        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(30000);

        // Set the connector
        server.addConnector(http);

        // Set a handler
        server.setHandler(new B70HelloHandler());

        // Start the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("bb");
                try {

                    server.start();
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
//        server.start();
//        while(true) {
//          Thread.sleep(100L);
//        }
        //server.join();
        Thread.sleep(10000L);
        System.out.println("aa");
        server.stop();
    }
}
