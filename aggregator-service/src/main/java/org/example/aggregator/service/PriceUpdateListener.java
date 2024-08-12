package org.example.aggregator.service;

import io.grpc.stub.StreamObserver;
import org.example.aggregator.dto.PriceUpdateDto;
import org.example.stock.PriceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class PriceUpdateListener implements StreamObserver<PriceUpdate> {

    private static final Logger log = LoggerFactory.getLogger(PriceUpdateListener.class);
    private final Set<SseEmitter> emitters = Collections.synchronizedSet(new HashSet<>());

    private final long sseTimeout;

    public PriceUpdateListener(@Value("${sse.timeout:300000}") long sseTimeout) {
        this.sseTimeout = sseTimeout;
    }

    public SseEmitter createEmitter(){
        var emitter = new SseEmitter(sseTimeout);
        emitters.add(emitter);
        emitter.onTimeout(()->this.emitters.remove(emitter));
        emitter.onError(ex -> this.emitters.remove(emitter));
        return emitter;
    }


    @Override
    public void onNext(PriceUpdate priceUpdate) {
        var dto = new PriceUpdateDto(priceUpdate.getTicker().name(), priceUpdate.getPrice());
        this.emitters.removeIf(e -> !this.send(e, dto));
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Streaming Error", throwable);
        emitters.forEach(e -> e.completeWithError(throwable));
        this.emitters.clear();

    }

    @Override
    public void onCompleted() {
        this.emitters.forEach(ResponseBodyEmitter::complete);
    }

    private boolean send(SseEmitter emitter, Object o){
        try {
            emitter.send(o);
            return true;
        } catch (IOException e) {
            log.warn("SSE Eror - {} ", e.getMessage());
            return false;
        }
    }
}
