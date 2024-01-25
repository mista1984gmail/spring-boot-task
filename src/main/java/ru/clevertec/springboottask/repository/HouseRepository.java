package ru.clevertec.springboottask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.springboottask.entity.model.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

   Optional<House> findByUuidHouse(UUID uuid);

   @Query(value = "SELECT * FROM houses WHERE to_tsvector(country) || to_tsvector(city) || to_tsvector(street) @@ plainto_tsquery(?1)", nativeQuery = true)
   List<House> findByCountryOrCityOrStreet(String name);

   @Query(value = "select * from houses as h where h.id in (select hh.house_id from house_history as hh where hh.person_type = 'TENANT' and hh.person_id = ?1)", nativeQuery = true)
   List<House> findAllHousesEverLivedPerson(Long id);

   @Query(value = "select * from houses as h where h.id in (select hh.house_id from house_history as hh where hh.person_type = 'OWNER' and hh.person_id = ?1)", nativeQuery = true)
   List<House> findAllHousesEverOwnedPerson(Long id);

}
