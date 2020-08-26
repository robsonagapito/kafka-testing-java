package support;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KafkaService {

    private static KafkaService instance = null;
    private static final String TOPIC_KEY = "app.kafka.topic.destino";

    public static synchronized KafkaService getInstance() {
        if (instance == null){
            instance = new KafkaService();
        }
        return instance;
    }

    public void sendMessage(Map<String, Object> props, String message) {
        KafkaProducer<String, String> producer = connectProducer(props);
        producer.send(new ProducerRecord<String, String>((String) props.get("app.kafka.topic.destino"), message),null);
        producer.close();
    }

    private KafkaProducer<String, String> connectProducer(Map<String, Object> props) {
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", StringSerializer.class);
        return new KafkaProducer(props);
    }

    private KafkaConsumer<String, String> connectConsumer(Map<String, Object> props) {
        props.put("group.id","consumerGroup");
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        return new KafkaConsumer(props);
    }

    public List<String> getMessages(Map<String, Object> props, int qty) {
        List<String> messages = new ArrayList<>();
        KafkaConsumer<String, String> consumer = connectConsumer(props);
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor((String) props.get(TOPIC_KEY));
        List<TopicPartition> topicPartitionList;
        String auxRecord = "";
        Long auxTime = Long.valueOf(0);
        Long saldo = Long.valueOf(0);

        for (PartitionInfo partitionInfo: partitionInfoList){
            TopicPartition topicPartition1 = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
            topicPartitionList = Arrays.asList(topicPartition1);

            consumer.assign(topicPartitionList);
            consumer.seekToEnd(topicPartitionList);

            for (TopicPartition topicPartition: topicPartitionList){
                long current = consumer.position(topicPartition);
                if ((current - qty) >= 0){
                    saldo = current - qty;
                } else {
                    saldo = current;
                }
                consumer.seek(topicPartition, saldo);
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record: records){
                    record.timestamp();
                    if (record.timestamp() > auxTime){
                        auxRecord = record.value();
                        auxTime = record.timestamp();
                    }
                }
            }
        }
        messages.add(auxRecord);
        consumer.close();
        return messages;
    }

}
