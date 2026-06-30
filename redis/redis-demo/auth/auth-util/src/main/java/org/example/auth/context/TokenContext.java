package org.example.auth.context;

public class TokenContext {

    private static ThreadLocal<String> tokenThreadLocal = new ThreadLocal<>();

    public static String get() {
        return tokenThreadLocal.get();
    }
    public static void set(String token) {
        tokenThreadLocal.set(token);
    }

    public static void clear() {
        tokenThreadLocal.remove();
    }
}
