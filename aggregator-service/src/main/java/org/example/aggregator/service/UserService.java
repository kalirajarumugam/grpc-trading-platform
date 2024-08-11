package org.example.aggregator.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.user.UserInformation;
import org.example.user.UserInformationRequest;
import org.example.user.UserServiceGrpc;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userClient;

    public UserInformation getUserInformation(int userId){

        var request = UserInformationRequest.newBuilder().setUserId(userId).build();
        return userClient.getUserInformation(request);
    }


}
