package org.example.demo.lambda;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder
public class Book {

    Long id;

    String name;

    List<BookType> typeList;
}
