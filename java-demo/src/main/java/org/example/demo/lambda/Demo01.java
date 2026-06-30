package org.example.demo.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Demo01 {

    public static void main(String[] args) {
        List<Author> authors = getAuthors();
        printAuthorsAgeLessThan(authors, 30);
    }

    static List<Author> getAuthors() {
        List<Author> list = new ArrayList<>();
        BookType[] bookTypes = BookType.values();
        AtomicLong bookIdGenerator = new AtomicLong();
        for(int i=0; i<10; i++) {
            // 生成book
            Random bookCountRandom = new Random(10);
            int bookCount = bookCountRandom.nextInt();
            List<Book> books = new ArrayList<>();
            for (int num=0; num< bookCount; num++) {

                //生成book type
                Random bookTypeCountRandom = new Random(3);
                int bookTypeCount = bookTypeCountRandom.nextInt();
                List<BookType> bookTypeList = new ArrayList<>();
                for(int j = 0; j < bookTypeCount; j++) {
                    bookTypeList.add(bookTypes[j]);
                }
                Book book = new Book.BookBuilder().id(bookIdGenerator.addAndGet(1))
                        .name("book" + bookIdGenerator.get())
                        .typeList(bookTypeList)
                        .build();
                books.add(book);
            }
            //生成作者
            Author author = new Author.AuthorBuilder().id((long) (i+1)).age(28+i).name("anonymous0" + i)
                    .books(books).build();
            list.add(author);
        }
        return list;
    }

    static void printAuthorsAgeLessThan(List<Author> authors, int benchmark) {
        authors.stream().distinct().filter(author -> author.age < benchmark)
                .forEach(author -> System.out.println("name:" + author.getName() + " age:" + author.getAge()));
    }
}
