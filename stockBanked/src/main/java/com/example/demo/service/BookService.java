package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookFilter;
import org.springframework.stereotype.Service;

import java.util.List;
public interface BookService {
    List<Book> getBook(BookFilter filter);
    Integer getBookNum(Integer bookId);
    void updateBookNum(Book book);
    void updateBookPrice(Book book);
    void insertBook(Book book);
}
