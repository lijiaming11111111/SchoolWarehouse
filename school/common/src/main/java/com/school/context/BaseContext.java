package com.school.context;

import java.util.List;

public class BaseContext {

    //当前用户ID
    public static ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static ThreadLocal<String> currentUserRoleId = new ThreadLocal<>();


    public static Long getCurrentUserId() {
        return currentUserId.get();
    }

    public static void setCurrentUserId(Long id) {
        currentUserId.set(id);
    }

    public static String getCurrentUserRoleRoleId() {
        return currentUserRoleId.get();
    }

    public static void setCurrentUserRoleRoleId(String roleId) {
        currentUserRoleId.set(roleId);
    }

    public static void clear() {
        currentUserId.remove();
        currentUserRoleId.remove();
    }
}
