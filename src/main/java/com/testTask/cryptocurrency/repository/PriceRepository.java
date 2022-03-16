package com.testTask.cryptocurrency.repository;

import com.testTask.cryptocurrency.model.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, String> {

    Page<Price> findAll(Pageable pageable);
}
