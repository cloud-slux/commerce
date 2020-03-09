package br.com.slux.Commerce;

import br.com.slux.Commerce.model.Item;
import br.com.slux.Commerce.model.Order;
import br.com.slux.Commerce.repository.ItemRepository;
import br.com.slux.Commerce.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JPAUnitTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void jpaTest() {
        Order order = new Order();
        order.setClient("test");
        order.setTotal(new BigDecimal(10));

        Item i1 = new Item();
        i1.setDescription("escova de dente");
        i1.setQuantity(new BigDecimal(2));
        i1.setUnitPrice(new BigDecimal(3));
        i1.setValue(new BigDecimal(6));
        i1.setOrder(order);

        Item i2 = new Item();
        i2.setDescription("pasta de dente");
        i2.setQuantity(new BigDecimal(1));
        i2.setUnitPrice(new BigDecimal(4));
        i2.setValue(new BigDecimal(4));
        i2.setOrder(order);

        List<Item> items = new ArrayList<>();
        items.add(i1);
        items.add(i2);

        orderRepository.save(order);
        Assertions.assertEquals(order.getId(), 1000);

        final String newClient = "test2";
        order.setClient(newClient);
        orderRepository.save(order);
        Order actualOrder = orderRepository.getOne(1000L);
        Assertions.assertEquals(actualOrder.getClient(), newClient);
        order = actualOrder;

        itemRepository.save(items.get(0));
        List<Item> actualItems = itemRepository.findAll();
        Assertions.assertEquals(actualItems.size(), 1);

        itemRepository.save(items.get(1));
        actualItems = itemRepository.findAll();
        Assertions.assertEquals(actualItems.size(), 2);

        final String newItemDescription = "barbeador";
        i1 = itemRepository.getOne(1L);
        i1.setDescription(newItemDescription);
        itemRepository.save(i1);
        Item actualItem = itemRepository.getOne(1L);
        Assertions.assertEquals(actualItem.getDescription(), newItemDescription);

        itemRepository.delete(items.get(1));
        actualItems = itemRepository.findAll();
        Assertions.assertEquals(actualItems.size(), 1);

        orderRepository.delete(order);

//        actualItems = itemRepository.findAll();
//        Assertions.assertEquals(actualItems.size(), 0);

        List<Order> actualOrders = orderRepository.findAll();
        Assertions.assertEquals(actualOrders.size(), 0);
    }

}

