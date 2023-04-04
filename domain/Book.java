package com.sandbox.java.lightspeed.domain;

import java.util.Arrays;
import java.util.Objects;

public class Book {
    private String title;
    private String[] authors;

    private Book(){}

    public static Book newInstance(String title, String[] authors) {
        return new Book().setTitle(title).setAuthors(authors);
    }

    public String getTitle() {
        return title;
    }

    public Book setTitle(String title) {
        this.title = title;
        return this;
    }

    public String[] getAuthors() {
        return authors;
    }

    public Book setAuthors(String[] authors) {
        this.authors = authors;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Arrays.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(title);
        result = 31 * result + Arrays.hashCode(authors);
        return result;
    }
}
