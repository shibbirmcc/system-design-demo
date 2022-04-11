package com.electrolux.demo.appliance;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class PingConnectionStatusStorageTask extends Thread{
  private static Logger logger = Logger.getLogger(PingConnectionStatusStorageTask.class.getName());
  private static final int MIN_SECONDS_TO_WAIT = 60;
  private static final int MAX_SECONDS_TO_WAIT = 180;

  private String storageServerUrl;
  private Random random = new Random();


  public PingConnectionStatusStorageTask(String storageServerUrl){
      this.storageServerUrl = storageServerUrl;
  }

  @Override
  public void run() {
    while(true){
      try{
        HttpURLConnection connection = (HttpURLConnection) (new URL(this.storageServerUrl)).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        logger.info("Response Code received from Storage Server: "+responseCode);
        if(responseCode != HttpURLConnection.HTTP_OK){
          logger.warning("Check if Storage Server is running!!");
        }
        connection.disconnect();
      }catch(Exception e){
        logger.severe("Error: "+e);
      }

      try{
        Thread.sleep(Duration.ofSeconds(getRandomWaitingTimeInSeconds()).toMillis());
      }catch(Exception e){
        logger.warning("Sleep Interruption Error: "+e);
      }

    }
  }


  private int getRandomWaitingTimeInSeconds() {
    return random.nextInt(MAX_SECONDS_TO_WAIT - MIN_SECONDS_TO_WAIT) + MIN_SECONDS_TO_WAIT;
  }
}
