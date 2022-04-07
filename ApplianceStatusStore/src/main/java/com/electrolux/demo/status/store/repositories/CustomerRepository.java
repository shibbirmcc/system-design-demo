package com.electrolux.demo.status.store.repositories;

import com.electrolux.demo.status.store.models.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
