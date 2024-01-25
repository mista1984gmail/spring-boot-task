package ru.clevertec.springboottask.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.springboottask.entity.enums.PersonType;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "house_history")
public class HouseHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "house_id", updatable = false)
	private Long houseId;

	@Column(name = "person_id", updatable = false)
	private Long personId;

	@Column(name = "create_date", updatable = false)
	private LocalDateTime createDate;

	@Column(name="person_type",
			nullable = false)
	@Enumerated(value = EnumType.STRING)
	private PersonType personType;

}