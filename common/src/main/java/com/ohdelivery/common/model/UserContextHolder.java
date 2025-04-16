package com.ohdelivery.common.model;

public class UserContextHolder {
    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void setCurrentUser(String currentUser) {
        userContext.set(currentUser);
    }

    public static String getCurrentUser() {
        return userContext.get();
    }

    public static void clear(){
        userContext.remove();
    }

}
