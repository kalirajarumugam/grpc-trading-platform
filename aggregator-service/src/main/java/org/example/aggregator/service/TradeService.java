package org.example.aggregator.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.stock.StockPriceRequest;
import org.example.stock.StockServiceGrpc;
import org.example.user.StockTradeRequest;
import org.example.user.StockTradeResponse;
import org.example.user.UserServiceGrpc;
import org.springframework.stereotype.Service;

@Service
public class TradeService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    @GrpcClient("stock-service")
    private StockServiceGrpc.StockServiceBlockingStub stockClient;

    public StockTradeResponse trade(StockTradeRequest request){

        var priceRequest = StockPriceRequest.newBuilder().setTicker(request.getTicker()).build();
        var priceResponse = stockClient.getStockPrice(priceRequest);

        var tradeRequest = request.toBuilder().setPrice(priceResponse.getPrice()).build();
        return userClient.tradeStock(tradeRequest);

    }

}
