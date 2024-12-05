package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookFilter;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookMapper bookMapper;
@Override
    public List<Book> getBook(BookFilter filter){
        List<Book> books=bookMapper.getBook(filter);
        return books;
    }
    @Override
    public Integer getBookNum(Integer bookId) {
        return bookMapper.getBookNum(bookId);
    }

    @Override
    public void updateBookNum(Book book) {
        bookMapper.updateBookNum(book);
    }
    @Override
    public void updateBookPrice(Book book) {
        bookMapper.updateBookPrice(book);
    }
    @Override
    public void insertBook(Book book) {

    }
}
