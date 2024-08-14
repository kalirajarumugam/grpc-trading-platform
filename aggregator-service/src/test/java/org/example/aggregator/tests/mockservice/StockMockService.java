package org.example.aggregator.tests.mockservice;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.common.Ticker;
import org.example.stock.PriceUpdate;
import org.example.stock.StockPriceRequest;
import org.example.stock.StockPriceResponse;
import org.example.stock.StockServiceGrpc;

public class StockMockService extends StockServiceGrpc.StockServiceImplBase {
    @Override
    public void getPriceUpdates(Empty request, StreamObserver<PriceUpdate> responseObserver) {

        for(int i = 0; i <= 5 ; i++) {
           var priceUpdate = PriceUpdate.newBuilder().setPrice(i).setTicker(Ticker.AMAZON).build();
           responseObserver.onNext(priceUpdate);
        }
        responseObserver.onCompleted();

    }

    @Override
    public void getStockPrice(StockPriceRequest request, StreamObserver<StockPriceResponse> responseObserver) {
        var response = StockPriceResponse.newBuilder().setPrice(15).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
