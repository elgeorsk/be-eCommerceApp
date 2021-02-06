package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    public static Item newItem(){
        Item item = new Item();

        item.setId(1l);
        item.setName("testProduct");
        item.setPrice(BigDecimal.valueOf(99.9));
        item.setDescription("testDescription");

        return item;
    }

    public static List<Item> listOfItems(){
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        Item item1 = new Item();
        Item item2 = new Item();

        item.setId(1l);
        item.setName("testProduct");
        item.setPrice(BigDecimal.valueOf(99.9));
        item.setDescription("testDescription");
        items.add(item);

        item1.setId(2l);
        item1.setName("testProduct2");
        item1.setPrice(BigDecimal.valueOf(199.9));
        item1.setDescription("testDescription2");
        items.add(item1);

        item2.setId(3l);
        item2.setName("testProduct3");
        item2.setPrice(BigDecimal.valueOf(299.9));
        item2.setDescription("testDescription3");
        items.add(item2);

        return items;
    }
}
