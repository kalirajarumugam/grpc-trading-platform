package org.example.service.advice;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.exceptions.InsufficientBalanceException;
import org.example.exceptions.InsufficientSharesException;
import org.example.exceptions.UnknownTickerException;
import org.example.exceptions.UnknownUserException;

@GrpcAdvice
public class ServiceExceptioHandler {

    @GrpcExceptionHandler(UnknownTickerException.class)
    public Status handleInvalidArguments(UnknownTickerException e){
        return Status.INVALID_ARGUMENT.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler(UnknownUserException.class)
    public Status handleUnknownEntities(UnknownUserException e){
        return Status.NOT_FOUND.withDescription(e.getMessage());
    }

    @GrpcExceptionHandler({InsufficientBalanceException.class, InsufficientSharesException.class})
    public Status handlePreConditionFailures(Exception e){
        return Status.FAILED_PRECONDITION.withDescription(e.getMessage());
    }


}
