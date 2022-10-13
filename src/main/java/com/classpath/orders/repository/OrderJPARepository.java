package com.classpath.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.classpath.orders.model.Order;

@Repository
public interface OrderJPARepository extends JpaRepository<Order, Long>{

}
