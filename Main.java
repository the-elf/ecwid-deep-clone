package com.sandbox.java.lightspeed;

import com.sandbox.java.lightspeed.domain.Book;
import com.sandbox.java.lightspeed.domain.Man;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        var authors1 = new String[]{"author1", "author2"};
        var authors2 = new String[]{"author3", "author2"};
        var books = new ArrayList<Book>();
        books.add(Book.newInstance("book1", authors1));
        books.add(Book.newInstance("book2", authors2));

        var man = new Man(null, 1, books);
        var copy = CopyUtils.deepCopy(man);

        System.out.println(copy.equals(man));
    }
}