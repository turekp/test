package pl.mediabit.test.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class KafkaFeeder {

    @Autowired
    private KafkaTemplate<String, Object> kt;

//    @PostConstruct
//    public void run() {
//        IntStream.range(0, 1000).forEach(
//                this::send
//        );
//    }

    public ListenableFuture<SendResult<String, Object>> send(int i) {
        return kt.send("test-topic", new ValueObject(i));
    }

}
