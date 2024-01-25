package ru.clevertec.springboottask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.springboottask.entity.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

   Optional<Person> findByUuidPerson(UUID uuid);

   @Modifying
   @Query(value = "delete from owners where person_id = ?", nativeQuery = true)
   void deleteOwnHouses(Long id);

   @Query(value = "select * from person as p where p.id in (select hh.person_id from house_history as hh where hh.person_type = 'TENANT' and hh.house_id = ?1)", nativeQuery = true)
   List<Person> findAllTenantsEverLivedInHouse(Long id);

   @Query(value = "select * from person as p where p.id in (select hh.person_id from house_history as hh where hh.person_type = 'OWNER' and hh.house_id = ?1)", nativeQuery = true)
   List<Person> findAllOwnersEverOwnedHouse(Long id);
}
