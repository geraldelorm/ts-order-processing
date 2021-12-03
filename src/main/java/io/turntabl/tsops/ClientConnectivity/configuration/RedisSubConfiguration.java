package io.turntabl.tsops.ClientConnectivity.configuration;

import io.turntabl.tsops.ClientConnectivity.service.MarketDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisSubConfiguration {
    @Value("${spring.redis.host}")
    String redisHostName;

    @Value("${spring.redis.port}")
    Integer redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHostName);
        redisStandaloneConfiguration.setPort(redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic marketDataFromExOne() {
        return new ChannelTopic("marketDataFromExOne");
    }

    @Bean
    public ChannelTopic marketDataFromExTwo(){
        return new ChannelTopic("marketDataFromExTwo");
    }

//    @Bean
//    public RedisTemplate redisTemplate() {
//        RedisTemplate<String, Product> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Product>(Product.class));
//        return template;
//    }

    @Bean("marketDataFromExOneListenerAdapter")
    MessageListenerAdapter marketDataFromExOneListenerAdapter() {
        return new MessageListenerAdapter(new MarketDataService(),"marketDataFromExOne");
    }

    @Bean("marketDataFromExTwoListenerAdapter")
    MessageListenerAdapter marketDataFromExTwoListenerAdapter() {
        return new MessageListenerAdapter(new MarketDataService(), "marketDataFromExTwo");
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(marketDataFromExOneListenerAdapter(), marketDataFromExOne());
        container.addMessageListener(marketDataFromExTwoListenerAdapter(), marketDataFromExTwo());
        return container;
    }
}

