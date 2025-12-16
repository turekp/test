package pl.mediabit.test.docker;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext
@EnableAutoConfiguration
@SpringBootTest(
        classes = AppTest.Config.class,
        properties = {
        // application.properties overrides
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.properties.schema.registry.url=mock://schema"
})
@EmbeddedKafka(
        partitions = 1,
        topics = AppTest.TEST_TOPIC,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:3333",
                "port=3333"
        })
class AppTest {

    public static final String TEST_TOPIC = "test-topic";
    public static final String GROUP_ID = "sender";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private Recorder recorder;

    @Test
    public void shouldSerializeValueObjectToAvroUsingSchema() throws InterruptedException {
        //when
        kafkaTemplate.send(TEST_TOPIC, new ValueObject(1));

        // then
        ConsumerRecord<String, GenericRecord> poll = recorder.poll(5, TimeUnit.SECONDS);
        assertNotNull(poll);
        assertThat(poll.value().get("number"), is(1));
    }

    public static class Recorder {

        private BlockingQueue<ConsumerRecord<String, GenericRecord>> records = new ArrayBlockingQueue<>(10);

        @KafkaListener(topics = TEST_TOPIC, groupId = GROUP_ID, properties = {
                "value.deserializer=io.confluent.kafka.serializers.KafkaAvroDeserializer",
                "schema.reflection=false",
                "schema.registry.url=mock://schema",
                "auto.offset.reset=earliest"
        })
        public void listen(ConsumerRecord<String, GenericRecord> message) {
            records.add(message);
        }

        public ConsumerRecord<String, GenericRecord> poll(long l, TimeUnit timeUnit) throws InterruptedException {
            return records.poll(l, timeUnit);
        }
    }

    public static class Config {

        @Bean
        public Recorder recorder() {
            return new Recorder();
        }

    }

}