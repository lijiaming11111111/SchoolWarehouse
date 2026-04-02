package server.handler;

import com.alibaba.fastjson.JSON;
import com.smartNotes.entity.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class ChatMessageSubscriber implements MessageListener {
    @Resource
    SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody());
        ChatMessage msg = JSON.parseObject(body, ChatMessage.class);
        template.convertAndSend("/topic/group/" + msg.getGroupId(), msg);
    }
}
