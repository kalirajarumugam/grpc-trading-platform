package org.example.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.service.handler.StockTradeRequestHandler;
import org.example.service.handler.UserInformationRequestHandler;
import org.example.user.StockTradeRequest;
import org.example.user.StockTradeResponse;
import org.example.user.UserInformation;
import org.example.user.UserInformationRequest;

@GrpcService
public class UserService extends org.example.user.UserServiceGrpc.UserServiceImplBase {

    private final UserInformationRequestHandler userRequestHandler;
    private final StockTradeRequestHandler tradeRequestHandler;

    public UserService(UserInformationRequestHandler userRequestHandler, StockTradeRequestHandler tradeRequestHandler) {
        this.userRequestHandler = userRequestHandler;
        this.tradeRequestHandler = tradeRequestHandler;
    }


    @Override
    public void getUserInformation(UserInformationRequest request, StreamObserver<UserInformation> responseObserver) {
       var userInformation = userRequestHandler.getUserInformation(request);
        responseObserver.onNext(userInformation);
        responseObserver.onCompleted();
    }

    @Override
    public void tradeStock(StockTradeRequest request, StreamObserver<StockTradeResponse> responseObserver) {
        var response = org.example.user.TradeAction.SELL.equals(request.getAction())?
                tradeRequestHandler.sellStock(request) :
                tradeRequestHandler.buyStock(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
