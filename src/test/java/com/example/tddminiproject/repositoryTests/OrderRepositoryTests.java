package com.example.tddminiproject.repositoryTests;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;



    @Test
    public void testSaveOrder(){
        Order orders = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);
        Order savedOrder = orderRepository.save(orders);

        Order existingOrder = entityManager.find(Order.class, savedOrder.getId());
        Assertions.assertNotNull(existingOrder);
        Assertions.assertEquals(existingOrder.getCustomerName(), orders.getCustomerName());
        Assertions.assertEquals(existingOrder.getShippingAddress(), orders.getShippingAddress());
        Assertions.assertEquals(existingOrder.getTotal(), orders.getTotal());
    }

    @Test
    public void testPass() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);

        Order savedOrder = orderRepository.save(order);

        Assertions.assertNotNull(savedOrder.getId());
    }

    @Test
    public void testFail() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);

        Order savedOrder = orderRepository.save(order);

        Assertions.assertNotNull(savedOrder.getId());
    }

    @Test
    public void testDeleteNonExistingOrder() {
        Long nonExistingOrderId = Long.MAX_VALUE;

        Optional<Order> order = orderRepository.findById(nonExistingOrderId);

        assertTrue(order.isEmpty());

        orderRepository.deleteById(nonExistingOrderId);

        Optional<Order> deletedOrder = orderRepository.findById(nonExistingOrderId);
        assertTrue(deletedOrder.isEmpty());
    }


    @Test
    public void testDeleteOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);
        Order savedOrder = entityManager.persistAndFlush(order);

        orderRepository.deleteById(savedOrder.getId());

        boolean orderExists = orderRepository.existsById(savedOrder.getId());
        Assertions.assertFalse(orderExists);
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);
        Order savedOrder = entityManager.persistAndFlush(order);

        savedOrder.setCustomerName("Jane Smith");
        savedOrder.setTotal(300.0);

        Order updatedOrder = orderRepository.save(savedOrder);

        Assertions.assertEquals(savedOrder.getCustomerName(), updatedOrder.getCustomerName());
        Assertions.assertEquals(savedOrder.getTotal(), updatedOrder.getTotal());
    }



    @Test
    public void testReadOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);
        Order savedOrder = entityManager.persistAndFlush(order);

        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        assertTrue(retrievedOrder.isPresent());
        Assertions.assertEquals(savedOrder.getId(), retrievedOrder.get().getId());
        Assertions.assertEquals(savedOrder.getCustomerName(), retrievedOrder.get().getCustomerName());
        Assertions.assertEquals(savedOrder.getShippingAddress(), retrievedOrder.get().getShippingAddress());
        Assertions.assertEquals(savedOrder.getTotal(), retrievedOrder.get().getTotal());
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street, City, State", 200.0);

        Order savedOrder = orderRepository.save(order);

        Assertions.assertNotNull(savedOrder.getId());
    }
    @Test
    public void testCreateOrder_ValidationErrors() {
        Order order = new Order("", null, "", -10.0);

        assertThrows(ConstraintViolationException.class, () -> orderRepository.save(order));
    }


}
