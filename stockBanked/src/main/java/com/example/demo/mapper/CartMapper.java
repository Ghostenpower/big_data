package com.example.demo.mapper;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartView;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface CartMapper {
    public List<CartView> getCart(Cart cart);

    void addCart(Cart cartRow);
    @Select("select cart_id from user_cart where user_id=#{user_id} limit 1")
    Integer getCartId(Integer userId);

    void updateCart(Cart cartRow);
    @Delete("delete from shopping_cart where cart_id=#{cart_id} and is_choose=1")
    void deleteCart(Cart cart);

    @Update("update shopping_cart set is_choose = #{is_choose} where cart_id=#{cart_id} and book_id=#{book_id}")
    void setChoose(Cart cartRow);
}
