package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartView;
import com.example.demo.entity.User;

import java.util.List;

public interface CartService {
    List<CartView> getCart(Cart cart);

    void addCart(Cart cartRow);

    Integer getCartId(Integer userId);

    void updateCart(Cart cartRow);

    void deleteCart(Cart cart);

    void setChoose(Cart cartRow);
}
