package org.example.user.tests;


import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.user.UserInformationRequest;
import org.example.user.UserServiceGrpc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest (
        properties = {
                "grpc.server.port=-1",
                "grpc.server.in-process-name=integration-test",
                "grpc.client.user-service.address=in-process:integration-test"
        }
)

public class UserServiceTest {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Test
    public void userInformation(){

        var request = UserInformationRequest.newBuilder()
                .setUserId(12).build();
        var response = stub.getUserInformation(request);

        System.out.println("response : " + response);

    }


}

