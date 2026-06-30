package org.example.demo.lambda;

import java.util.Arrays;
import java.util.List;

public enum BookType {
    YAN_QING("言情"),
    HISTORY("历史"),
    WU_XIA("武侠");

    private final String name;

    BookType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public List<BookType> getAll() {
        return Arrays.asList(YAN_QING, HISTORY, WU_XIA);
    }
}
