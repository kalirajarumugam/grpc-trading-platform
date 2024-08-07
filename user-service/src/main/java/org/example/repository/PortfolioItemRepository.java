package org.example.repository;

import org.example.entity.PortfolioItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioItemRepository extends CrudRepository<PortfolioItem, Integer> {

    List<PortfolioItem> findAllByUserId(Integer userId);
    Optional<PortfolioItem> findByUserIdAndTicker(Integer userId, org.example.common.Ticker ticker);



}
