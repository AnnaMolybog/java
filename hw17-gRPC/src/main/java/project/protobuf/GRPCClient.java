package project.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.protobuf.generated.GenerateSequenceServiceGrpc;
import project.protobuf.generated.SequenceRequest;
import project.protobuf.generated.SequenceResponse;

import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GRPCClient {

    private static final int NUMBER_OF_CLIENT_ITERATIONS = 50;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private static final Stack<SequenceResponse> valuesFromServer = new Stack<>();
    private static long currentValue = 0;
    private static int counter = 0;

    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = GenerateSequenceServiceGrpc.newStub(channel);

        logger.info("Sequences of numbers Client is starting....");

        stub.generateSequence(
                SequenceRequest.newBuilder().setFirstValue(0).setLastValue(30).build(),
                getStreamObserver()
        );

        executorService.scheduleAtFixedRate(
                () -> {
                    processSequence();
                    counter++;
                    if (counter == NUMBER_OF_CLIENT_ITERATIONS) {
                        onComplete(channel);
                    }
                },
                1,
                1,
                TimeUnit.SECONDS
        );
    }

    private static void processSequence()
    {
        long lastValueFromServer = !valuesFromServer.isEmpty() ? valuesFromServer.pop().getNewValue() : 0;
        currentValue = currentValue + lastValueFromServer + 1;
        logger.info("current value: " + currentValue);
    }

    private static StreamObserver<SequenceResponse> getStreamObserver()
    {
        return new StreamObserver<>() {
            @Override
            public void onNext(SequenceResponse sequenceResponse) {
                valuesFromServer.push(sequenceResponse);
                logger.info("new value:" + sequenceResponse.getNewValue());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("ERROR " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Done!!!");
            }
        };
    }

    private static void onComplete(ManagedChannel channel)
    {
        logger.info("Request was completed");
        executorService.shutdown();
        channel.shutdown();
    }
}
