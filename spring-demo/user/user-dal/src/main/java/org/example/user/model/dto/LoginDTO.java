package org.example.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 必须加！否则 MyBatis/反射会报错
@AllArgsConstructor // 必须加！否则 Builder 无法生成
public class LoginDTO {
    String token;
}
