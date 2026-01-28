package org.example.orderservice.repository;

import org.example.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findDistinctByPizzaIdsIn(Collection<Long> pizzaIds, Pageable pageable);
}
