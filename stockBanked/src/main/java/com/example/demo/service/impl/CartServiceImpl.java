package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartView;
import com.example.demo.entity.User;
import com.example.demo.mapper.CartMapper;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    public List<CartView> getCart(Cart cart){
        List<CartView> list=cartMapper.getCart(cart);
        return list;
    }

    @Override
    public void addCart(Cart cartRow) {
        cartMapper.addCart(cartRow);
    }

    @Override
    public Integer getCartId(Integer userId) {
        return cartMapper.getCartId(userId);
    }

    @Override
    public void updateCart(Cart cartRow) {
        cartMapper.updateCart(cartRow);
    }

    @Override
    public void deleteCart(Cart cart) {
        cartMapper.deleteCart(cart);
    }

    @Override
    public void setChoose(Cart cartRow) {
        cartMapper.setChoose(cartRow);
    }
}
