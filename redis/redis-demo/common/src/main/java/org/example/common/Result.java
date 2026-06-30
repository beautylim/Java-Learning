package org.example.common;

import lombok.Data;

@Data
public class Result<T> {

    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.data = data;
        return result;
    }
}
