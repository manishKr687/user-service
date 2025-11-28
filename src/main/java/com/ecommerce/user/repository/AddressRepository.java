
package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface defines the repository for the {@link Address} entity.
 * It extends {@link JpaRepository}, which provides standard CRUD (Create, Read, Update, Delete) operations.
 * Custom query methods for the Address entity can be defined here.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {}
