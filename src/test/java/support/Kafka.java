package support;

import config.Configuration;
import org.aeonbits.owner.ConfigCache;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;
import steps.Hooks;
import java.util.*;

public class Kafka {

    private static final Configuration configuration = ConfigCache.getOrCreate(Configuration.class);

    public void sendMessage(String message, String topic){
        KafkaService service = KafkaService.getInstance();
        printLog("SEND MESSAGE", topic, message);
        service.sendMessage(configKafka(configuration.getBrockers(), topic),message);
    }

    public String getLastMessage(String topic){
        KafkaService service = KafkaService.getInstance();
        List<String> registros = service.getMessages(configKafka(configuration.getBrockers(), topic), 1);
        printLog("GET MESSAGE", topic, registros.toString());
        return registros.get(0);
    }

    private Map<String, Object> configKafka(String server, String topic){
        Map<String, Object> props = new LinkedHashMap();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, server);
        props.put("app.kafka.topic.destino", topic);
        if (configuration.getSecurity()) {
            System.setProperty("sun.security.krb5.debug",configuration.getKrb5Debug());
            System.setProperty("java.security.krb5.realm", configuration.getKrb5Realm());
            System.setProperty("java.security.krb5.kdc", configuration.getKrb5Kdc());
            System.setProperty("java.security.auth.login.config", configuration.getAuthLoginConfig());
            System.setProperty("java.security.krb5.conf", configuration.getKrb5Conf());
            props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, configuration.getFileJks());
            props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, configuration.getFileJks());
            props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, configuration.getPassword());
            props.put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, configuration.getPassword());
            props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, configuration.getPassword());
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, configuration.getConfSecurityProtocol());
            props.put("sasl.mechanism", configuration.getSaslMechanism());
        }
        return props;
    }

    private static void printLog(String type, String topic, String message){
        System.out.println("");
        System.out.println("------------------------------------");
        System.out.println("");
        System.out.println("TYPE: [ "+ type + " ]");
        System.out.println("TOPIC: [ "+ topic + " ]");
        System.out.println("MESSAGE: [ "+ message + " ]");
        System.out.println("");
        System.out.println("====================================");

        Hooks.scenario.write("------------------------------------");
        Hooks.scenario.write("TYPE: [ "+ type + " ]");
        Hooks.scenario.write("TOPIC: [ "+ topic + " ]");
        Hooks.scenario.write("MESSAGE: [ "+ message + " ]");
        Hooks.scenario.write("");
    }
}
