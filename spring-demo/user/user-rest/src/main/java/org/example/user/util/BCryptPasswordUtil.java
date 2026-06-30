package org.example.user.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptPasswordUtil {

    public static String hashPassword(String plainPassword) {
        // 自动生成盐 + 哈希，一步到位
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * 登录时使用：校验明文密码与数据库中的哈希是否匹配
     * @param plainPassword 用户输入的明文密码
     * @param hashedPassword 数据库存的BCrypt哈希
     * @return 匹配返回true，不匹配返回false
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
