package com.example.demo.mapper;

import com.example.demo.entity.Book;
import com.example.demo.entity.BookFilter;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface BookMapper {
    public List<Book> getBook(BookFilter filter);

    void insertBook(Book book);
    @Select("select quantity from books where book_id=#{bookId}")
    Integer getBookNum(Integer bookId);
    @Update("update books set price=#{price} where book_id=#{book_id}")
    void updateBookPrice(Book book);
    @Update("update books set quantity=#{quantity} where book_id=#{book_id}")
    void updateBookNum(Book book);
}
