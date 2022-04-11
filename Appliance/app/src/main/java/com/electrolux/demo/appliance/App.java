package com.electrolux.demo.appliance;

import java.util.logging.Logger;

public class App {
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        if(args.length == 0){
            logger.info("Usage: java -jar Appliance http://storage-server-host:port/ping/{applianceId}");
            return;
        }
        String storageServerUrl = args[0];
        logger.info("storageServerUrl: "+storageServerUrl);
        PingConnectionStatusStorageTask pingConnectionStatusStorageTask = new PingConnectionStatusStorageTask(storageServerUrl);
        pingConnectionStatusStorageTask.start();
    }
}
