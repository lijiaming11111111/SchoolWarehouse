package server.interceptor;

import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserManager {
    // 存储在线用户ID（或用户名）
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String userId) {
        onlineUsers.add(userId);
    }

    public void removeUser(String userId) {
        onlineUsers.remove(userId);
    }

    public Set<String> getOnlineUsers() {
        return Collections.unmodifiableSet(onlineUsers);
    }

    public boolean isOnline(String userId) {
        return onlineUsers.contains(userId);
    }
}