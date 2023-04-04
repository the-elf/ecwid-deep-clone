package com.sandbox.java.lightspeed.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Man {
    private String name;
    private int age;
    private List<Book> favoriteBooks;
    private Map<Integer, String> favBooksByNumber;
    private Type type;

    public Man(String name, int age, List<Book> favoriteBooks) {
        this.name = name;
        this.age = age;
        this.favoriteBooks = favoriteBooks;
        this.favBooksByNumber = favoriteBooks == null || favoriteBooks.isEmpty()
            ? null
            : IntStream.range(0, favoriteBooks.size())
                       .boxed()
                       .collect(Collectors.toMap(Function.identity(), it -> favoriteBooks.get(it).getTitle()));
        this.type = Type.TYPE_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }

    public void setFavoriteBooks(List<Book> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
    }

    public Map<Integer, String> getFavBooksByNumber() {
        return favBooksByNumber;
    }

    public void setFavBooksByNumber(Map<Integer, String> favBooksByNumber) {
        this.favBooksByNumber = favBooksByNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Man)) return false;
        Man man = (Man) o;
        return age == man.age
            && Objects.equals(name, man.name)
            && Objects.equals(favoriteBooks, man.favoriteBooks)
            && Objects.equals(favBooksByNumber, man.favBooksByNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, favoriteBooks, favBooksByNumber);
    }
}
