package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    // every before single test
    @BeforeEach
    public void setUp(){
        log.info("-- Init --");
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    // sanity test for user creation
    @Test
    @Order(1)
    @DisplayName("Check addToCart method")
    public void addToCartTest() throws Exception{
        log.info("-- Start of test  --");
        log.info("Get user");
        User user = TestUtils.newUser();
        log.info("Get item");
        Item item = TestUtils.newItem();

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        cartRequest.setItemId(item.getId());
        cartRequest.setQuantity(1);

        when(userRepository.findByUsername(cartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(cartRequest.getItemId())).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertEquals(1, responseEntity.getBody().getItems().size());
        assertEquals(item.getName(), responseEntity.getBody().getItems().get(0).getName());
        log.info("-- End of test  --");
    }

    // sanity test for user creation
    @Test
    @Order(2)
    @DisplayName("Check removeFromCart method")
    public void removeFromCartTest() throws Exception{
        log.info("-- Start of test  --");
        log.info("Get user");
        User user = TestUtils.newUser();
        log.info("Get list of items");
        List<Item> items = TestUtils.listOfItems();

        Cart userCart = user.getCart();
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(user.getUsername());
        for(Item item: items){
            userCart.addItem(item);
            userCart.getTotal().add(item.getPrice()); // add price to the total

            cartRequest.setItemId(item.getId());
            cartRequest.setQuantity(1);
        }

        Item removedItem = items.get(0);
        BigDecimal total = userCart.getTotal().subtract(items.get(0).getPrice());

        when(userRepository.findByUsername(cartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(cartRequest.getItemId())).thenReturn(java.util.Optional.of(removedItem));

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        assertEquals(2, responseEntity.getBody().getItems().size());
        assertEquals(total, responseEntity.getBody().getTotal());
        log.info("-- End of test  --");
    }
}
