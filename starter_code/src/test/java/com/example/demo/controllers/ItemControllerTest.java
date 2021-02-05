package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private static final Logger log = LoggerFactory.getLogger(ItemControllerTest.class);

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    // every before single test
    @BeforeEach
    public void setUp(){
        log.info("-- Init --");
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    @Order(1)
    @DisplayName("Check getItems method")
    public void checkGetItems(){
        log.info("-- Start of test  --");
        List<Item> items = TestUtils.listOfItems();
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertArrayEquals(items.toArray(), responseEntity.getBody().toArray());
        log.info("-- End of test  --");
    }

    @Test
    @Order(2)
    @DisplayName("Check getItemById method")
    public void checkGetItemById(){
        log.info("-- Start of test  --");
        Item item = TestUtils.newItem();
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));
        ResponseEntity<Item> responseEntity = itemController.getItemById(item.getId());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(item.getName(), responseEntity.getBody().getName());
        assertEquals(item.getDescription(), responseEntity.getBody().getDescription());
        log.info("-- End of test  --");
    }

    @Test
    @Order(3)
    @DisplayName("Check getItemsByName method")
    public void checkGetItemsByName(){
        log.info("-- Start of test  --");
        Item item = TestUtils.newItem();
        List<Item> items = TestUtils.listOfItems();
        when(itemRepository.findByName(item.getName())).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(items.get(0).getName());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(item.getName(), responseEntity.getBody().get(0).getName());
        log.info("-- End of test  --");
    }
}
