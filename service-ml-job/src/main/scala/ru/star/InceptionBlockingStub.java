package ru.star;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ExperimentalApi;
import io.grpc.MethodDescriptor;
import io.grpc.MethodDescriptor.MethodType;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.AbstractStub;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static ru.star.InceptionInference.InceptionRequest;
import static ru.star.InceptionInference.InceptionResponse;

/**
 * Stub implementation for {@link #SERVICE_NAME} (Tensorflow' Inception service )
 */
public class InceptionBlockingStub extends AbstractStub<InceptionBlockingStub> {

    public static final String SERVICE_NAME = "tensorflow.serving.InceptionService";
    public static final String METHOD_NAME = "Classify";

    @ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
    public static final MethodDescriptor<InceptionRequest, InceptionResponse> METHOD_DESCRIPTOR = MethodDescriptor
            .create(MethodType.UNARY,
                    generateFullMethodName(SERVICE_NAME, METHOD_NAME),
                    ProtoUtils.marshaller(InceptionRequest.getDefaultInstance()),
                    ProtoUtils.marshaller(InceptionResponse.getDefaultInstance()));

    public InceptionBlockingStub(Channel channel) {
        super(channel);
    }

    private InceptionBlockingStub(Channel channel, CallOptions callOptions) {
        super(channel, callOptions);
    }

    @Override
    protected InceptionBlockingStub build(Channel channel,
                                          CallOptions callOptions) {
        return new InceptionBlockingStub(channel, callOptions);
    }

    public InceptionResponse classify(InceptionRequest request) {
        return blockingUnaryCall(getChannel(), METHOD_DESCRIPTOR, getCallOptions(),
                request);
    }
}
