package com.ohdelivery.common.passport.usercontext;

import com.ohdelivery.common.passport.Passport;

public class UserContextHolder {
    private static final ThreadLocal<Passport> userContext = new ThreadLocal<>();

    public static void setPassport(Passport passport) {
        userContext.set(passport);
    }

    public static Passport getPassport() {
        return userContext.get();
    }

    public static void clear(){
        userContext.remove();
    }

}
