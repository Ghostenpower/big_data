package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@RequestMapping("/book")
@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @RequestMapping(value="/getBook",method = RequestMethod.POST)
    public Result getBook(@RequestBody BookFilter filter){
        List<Book> books=bookService.getBook(filter);
        return Result.success(books);
    }
    @RequestMapping(value = "/updateBookNum",method = RequestMethod.POST)
    public Result buyBook(@RequestBody Book book){
        bookService.updateBookNum(book);
        return Result.success("success");
    }
    @RequestMapping(value ="/updatePrice",method = RequestMethod.POST)
    public  Result updatePrice(@RequestBody Book book){
        bookService.updateBookPrice(book);
        return Result.success("success");
    }
}
