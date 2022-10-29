package project.protobuf;


import io.grpc.ServerBuilder;
import project.protobuf.service.GenerateSequenceServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var service = new GenerateSequenceServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(service).build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
