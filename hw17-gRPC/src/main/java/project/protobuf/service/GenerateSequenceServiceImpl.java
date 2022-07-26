package project.protobuf.service;

import io.grpc.stub.StreamObserver;
import project.protobuf.generated.GenerateSequenceServiceGrpc;
import project.protobuf.generated.SequenceRequest;
import project.protobuf.generated.SequenceResponse;

import java.util.concurrent.*;

public class GenerateSequenceServiceImpl extends GenerateSequenceServiceGrpc.GenerateSequenceServiceImplBase {
    private final ScheduledExecutorService dataGenerationThreadPool = Executors.newScheduledThreadPool(1);
    private BlockingQueue<SequenceResponse> queue;
    @Override
    public void generateSequence(SequenceRequest request, StreamObserver<SequenceResponse> responseObserver) {
        queue = new ArrayBlockingQueue<>((int) request.getLastValue());
        for (var i = request.getFirstValue() + 1; i <= request.getLastValue(); i++) {
            SequenceResponse sequenceResponse = SequenceResponse.newBuilder()
                    .setNewValue(i)
                    .build();
            queue.offer(sequenceResponse);
        }

        poll(responseObserver);
    }

    private void poll(StreamObserver<SequenceResponse> responseObserver) {
        dataGenerationThreadPool.scheduleWithFixedDelay(
                () -> {
                    try {
                        responseObserver.onNext(queue.take());
                        if (queue.isEmpty()) {
                            responseObserver.onCompleted();
                            dataGenerationThreadPool.shutdown();
                        }
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                },
                2,
                2,
                TimeUnit.SECONDS
        );
    }
}
