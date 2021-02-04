package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;

        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.canAccess(target)){
                f.setAccessible(true);
                wasPrivate = true;
            }

            f.set(target, toInject);

            if(wasPrivate){
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User newUser(){
        User user = new User();
        user.setId(1l);
        user.setUsername("test");
        user.setPassword("testpass");

        Cart cart  = new Cart();
        cart.setId(1l);
        cart.setUser(user);
        cart.setTotal(new BigDecimal(0));

        user.setCart(cart);

        return user;
    }

}
