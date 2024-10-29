package ru.practicum.events.controller.gRPC;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.configuration.ConfigurationHandlers;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;


@GrpcService
@Slf4j
@RequiredArgsConstructor
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final ConfigurationHandlers configurationHandlers;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Received sensor event: {}", request);

        try {
            if (configurationHandlers.getSensorEventHandlersRPC().containsKey(request.getPayloadCase())) {
                configurationHandlers.getSensorEventHandlersRPC().get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("There is no handler for the event " + request.getPayloadCase());
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Received hub event: {}", request);

        try {
            if (configurationHandlers.getHubEventHandlersRPC().containsKey(request.getPayloadCase())) {
                configurationHandlers.getHubEventHandlersRPC().get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("There is no handler for the event " + request.getPayloadCase());
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}