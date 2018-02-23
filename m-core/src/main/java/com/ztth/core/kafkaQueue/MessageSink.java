package com.ztth.core.kafkaQueue;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageSink {

    String INPUT = "input";

    String INPUT0 = "input0";

    String INPUT1 = "input1";

    String INPUT2 = "input2";

    @Input(INPUT0)
    SubscribableChannel input0();

    @Input(INPUT1)
    SubscribableChannel input1();

    @Input(INPUT2)
    SubscribableChannel input2();

    @Input(INPUT)
    SubscribableChannel input();
}
