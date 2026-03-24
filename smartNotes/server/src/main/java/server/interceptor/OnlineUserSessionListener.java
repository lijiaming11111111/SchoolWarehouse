package server.interceptor;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class OnlineUserSessionListener implements HttpSessionListener {

    private final OnlineUserManager onlineUserManager;

    public OnlineUserSessionListener(OnlineUserManager onlineUserManager) {
        this.onlineUserManager = onlineUserManager;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 会话创建时，可在登录后将用户ID存入Session
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        Object userIdObj = session.getAttribute("userId");

        if (userIdObj != null) {
            String userId = userIdObj.toString();
            onlineUserManager.removeUser(userId);
        }
    }
}
