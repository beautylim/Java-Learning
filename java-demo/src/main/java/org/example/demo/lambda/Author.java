package org.example.demo.lambda;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder
public class Author {

    Long id;

    String name;

    int age;

    List<Book> books;

}
