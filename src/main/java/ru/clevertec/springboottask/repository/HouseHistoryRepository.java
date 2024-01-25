package ru.clevertec.springboottask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.springboottask.entity.model.HouseHistory;

@Repository
public interface HouseHistoryRepository extends JpaRepository<HouseHistory, Long> {

}
