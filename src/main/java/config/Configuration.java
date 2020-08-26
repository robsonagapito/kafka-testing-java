package config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

@Config.Sources({"classpath:conf/kafka.properties"})
public interface Configuration extends Config {

    @Key("kafka.brockers")
    String getBrockers();

    @Key("kafka.security")
    Boolean getSecurity();

    @Key("kafka.security.pass")
    String getPassword();

    @Key("kafka.security.hostRealm")
    String getHostRealm();

    @Key("kafka.security.hostKdc")
    String getHostKdc();

    @Key("kafka.security.krb5.debug")
    String getKrb5Debug();

    @Key("kafka.security.krb5.realm")
    String getKrb5Realm();

    @Key("kafka.security.krb5.kdc")
    String getKrb5Kdc();

    @Key("kafka.security.auth.login.config")
    String getAuthLoginConfig();

    @Key("kafka.security.krb5.conf")
    String getKrb5Conf();

    @Key("kafka.security.file.jks")
    String getFileJks();

    @Key("kafka.CommonClientConfigs.securityProtocol")
    String getConfSecurityProtocol();

    @Key("kafka.sasl.mechanism")
    String getSaslMechanism();

}
