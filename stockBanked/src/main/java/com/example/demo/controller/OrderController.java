package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderService;
import com.example.demo.util.JWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;
    @Autowired
    CartService cartService;
    @Autowired
    BookService bookService;



    @PostMapping("subOrder")
    public Result subOrder(HttpServletRequest request, @RequestBody OrderDetail orderDetail){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer user_id = (Integer) claims.get("user_id");
        Order order=new Order();
        order.setUser_id(user_id);
        order.setStatus("Pending");
        order.setTotal_price(orderDetail.getTotal_price());
        orderService.createOrder(order);
            orderDetail.setOrder_id(order.getOrder_id());
            System.out.println(orderDetail);
            orderDetailService.insertOrderDetail(orderDetail);
            Book book=new Book();
            book.setBook_id(orderDetail.getBook_id());
            book.setQuantity(bookService.getBookNum(orderDetail.getBook_id())-orderDetail.getQuantity());
            System.out.println(book);
            bookService.updateBookNum(book);
        return Result.success("success");
    }

    @PostMapping("/getOrder")
    public Result getOrder(HttpServletRequest request,@RequestBody Order order) {
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer user_id = (Integer) claims.get("user_id");
        order.setUser_id(user_id);
        List<Order> list = orderService.getOrder(order);
        return Result.success(list);
    }

    @PostMapping("subCartOrder")
    public Result subCartOrder(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer user_id = (Integer) claims.get("user_id");
        Integer cart_id = (Integer) claims.get("cart_id");
        Order order=new Order();
        order.setUser_id(user_id);
        order.setStatus("Pending");
        order.setTotal_price(0.00);
        System.out.println(order);
        orderService.createOrder(order);

        Cart cartf=new Cart();
        cartf.setCart_id(cart_id);
        cartf.setIs_choose(true);
        List<CartView> cartList=cartService.getCart(cartf);
        boolean notAll=false;
        Double allMoney=0.0;
        for(CartView cartRow:cartList){
            Book book=new Book();
            if(cartRow.getQuantity()>bookService.getBookNum(cartRow.getBook_id())){
                notAll=true;
            }else{
                book.setBook_id(cartRow.getBook_id());
                book.setQuantity(bookService.getBookNum(cartRow.getBook_id())-cartRow.getQuantity());
                bookService.updateBookNum(book);
                cartRow.setCart_id(cart_id);
                Cart cart=new Cart();
                cart.setCart_id(cartRow.getCart_id());
                cart.setBook_id(cartRow.getBook_id());
                cartService.deleteCart(cart);
                OrderDetail orderDetail=new OrderDetail();
                orderDetail.setOrder_id(order.getOrder_id());
                orderDetail.setBook_id(cartRow.getBook_id());
                orderDetail.setQuantity(cartRow.getQuantity());
                orderDetail.setTotal_price(cartRow.getTotal_price());
                orderDetailService.insertOrderDetail(orderDetail);
                allMoney+=orderDetail.getTotal_price();
            }
        }
        order.setTotal_price(allMoney);
        orderService.updatePrice(order);
        if(notAll){
            return Result.success("部分商品由于库存变动未提交，已为您更新购物车");
        }
        else{
            return Result.success("success");
        }

    }
}