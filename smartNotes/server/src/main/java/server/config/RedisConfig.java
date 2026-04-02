package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import server.handler.ChatMessageSubscriber;

@Configuration
public class RedisConfig {
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(adapter, new PatternTopic("chat:*"));
        return container;
    }

    @Bean
    public MessageListenerAdapter adapter(ChatMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }
}
