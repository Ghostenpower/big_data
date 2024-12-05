package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartView;
import com.example.demo.entity.Result;
import com.example.demo.service.BookService;
import com.example.demo.service.CartService;
import com.example.demo.util.JWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ShoppingCartController {
    @Autowired
    CartService cartService;
    @Autowired
    BookService bookService;


    @PostMapping("getCart")
        public Result getCart(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer cart_id=(Integer) claims.get("cart_id");
        Cart cart=new Cart();
        cart.setCart_id(cart_id);
        List<CartView> list= cartService.getCart(cart);
        for(CartView cartView:list){
            if (cartView.getQuantity()>bookService.getBookNum(cartView.getBook_id())){
                cartView.setQuantity(bookService.getBookNum(cartView.getBook_id()));
                Cart cart1=new Cart();
                cart1.setCart_id(cartView.getCart_id());
                cart1.setIs_choose(cartView.getIs_choose());
                cart1.setQuantity(bookService.getBookNum(cartView.getBook_id()));
                cart1.setBook_id(cartView.getBook_id());
                cart1.setTotal_price(cartView.getTotal_price());
                cartService.updateCart(cart1);
            }
        }
        list=cartService.getCart(cart);
        System.out.println(list);
        return Result.success(list);
    }
//
@PostMapping("addCart")
public Result addCart(HttpServletRequest request,@RequestBody Cart cartRow){
        Integer n=bookService.getBookNum(cartRow.getBook_id());
        if(n<cartRow.getQuantity()){
            return Result.error("库存不足");
        }else if(cartRow.getQuantity()<=0){
            return Result.error("提交数量错误");
        }
        else {
            String token = request.getHeader("token");
            Map<String, Object> claims = JWT.parseJWT(token);
            Integer cart_id=(Integer) claims.get("cart_id");
            cartRow.setCart_id(cart_id);
            List<CartView> cartlist=cartService.getCart(cartRow);
            if(cartlist.isEmpty()){
                cartService.addCart(cartRow);
            }
            else {
                cartRow.setQuantity(cartRow.getQuantity()+cartlist.get(0).getQuantity());
                if(cartRow.getQuantity()>bookService.getBookNum(cartRow.getBook_id())){
                    return Result.error("库存不足");
                }
                cartRow.setTotal_price(cartRow.getTotal_price()+cartlist.get(0).getTotal_price());
                cartRow.setCart_id(cart_id);
                cartService.updateCart(cartRow);
            }
            return Result.success("success");
        }

}

    @PostMapping("updateCart")
    public Result updateCart(HttpServletRequest request,@RequestBody Cart cart){
        Integer n=bookService.getBookNum(cart.getBook_id());
        if(n<cart.getQuantity()){
            return Result.error("库存不足");
        }else if(cart.getQuantity()<0){
            return Result.error("提交数量错误");
        }
        else {
            if(cart.getQuantity()==0){
                cart.setIs_choose(true);
                cartService.setChoose(cart);
                cartService.deleteCart(cart);
                return Result.success("success");
            }else{
                List<CartView> list=cartService.getCart(cart);
                if(list.isEmpty()){
                    cartService.addCart(cart);
                }else{
                    cartService.updateCart(cart);

                }
                return Result.success("success");
            }
        }
    }

    @PostMapping("setChoose")
    public Result setChoose(HttpServletRequest request,@RequestBody List<Cart> list){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer cart_id = (Integer) claims.get("cart_id");
        for(Cart cartRow:list){
            cartRow.setCart_id(cart_id);
            cartService.setChoose(cartRow);
        }
        return Result.success("success");
    }



    @PostMapping("getCartId")
    public Result pushCart(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer cart_id = (Integer) claims.get("cart_id");
        return Result.success(cart_id);
    }

    @PostMapping("deleteCart")
    public Result deleteCart(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Object> claims = JWT.parseJWT(token);
        Integer cart_id = (Integer) claims.get("cart_id");
        Cart cart=new Cart();
        cart.setCart_id(cart_id);
        cartService.deleteCart(cart);

        return Result.success("success");
    }

}
