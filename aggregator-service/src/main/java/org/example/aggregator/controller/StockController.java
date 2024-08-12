package org.example.aggregator.controller;

import com.google.common.util.concurrent.Uninterruptibles;
import org.example.aggregator.service.PriceUpdateListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("stock")
public class StockController {

    @Autowired
    private PriceUpdateListener listener;

    @GetMapping(value = "updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter priceUpdates(){
        return listener.createEmitter();

       /* SseEmitter emitter = new SseEmitter(50000L);

        Runnable runnable = () -> {
          for(int i=0; i<150; i++){
              Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);

              try {
                  emitter.send("hello - " + i);
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
          }
          emitter.complete();

        };

        new Thread(runnable).start();

        return emitter;*/

    }

}
