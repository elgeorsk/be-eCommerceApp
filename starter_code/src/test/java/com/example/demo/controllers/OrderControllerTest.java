package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    // every before single test
    @BeforeEach
    public void setUp(){
        log.info("-- Init --");
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    // sanity test for user creation
    @Test
    @Order(1)
    @DisplayName("Check submit method")
    public void submitTest() throws Exception{
        log.info("-- Start of test  --");
        log.info("Get user");
        User user = TestUtils.newUser();
        log.info("Get list of items");
        List<Item> items = TestUtils.listOfItems();

        Cart userCart = user.getCart();
        for(Item item: items){
            userCart.addItem(item);
            userCart.getTotal().add(item.getPrice()); // add price to the total
        }

        user.setCart(userCart);
        userCart.setUser(user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(user.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertEquals(3, responseEntity.getBody().getItems().size());
        assertEquals(userCart.getTotal(), responseEntity.getBody().getTotal());
        log.info("-- End of test  --");
    }

    @Test
    @Order(2)
    @DisplayName("Check getOrdersForUser method")
    public void getOrdersForUserTest() throws Exception{
        log.info("-- Start of test  --");
        log.info("Get user");
        User user = TestUtils.newUser();
        log.info("Get list of items");
        List<Item> items = TestUtils.listOfItems();

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setItems(items);
        userOrder.setTotal(BigDecimal.ZERO);
        
        for(Item item: items) {
            userOrder.getTotal().add(item.getPrice()); // add price to the total
        }

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(userOrder));
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertEquals(1, responseEntity.getBody().size());
        assertEquals(userOrder.getTotal(), responseEntity.getBody().get(0).getTotal());
        log.info("-- End of test  --");
    }
}
