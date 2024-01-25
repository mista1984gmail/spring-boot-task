package ru.clevertec.springboottask.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import ru.clevertec.springboottask.entity.enums.Sex;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "person")
@SQLDelete(sql = "UPDATE person SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name="id",
			updatable = false)
	private Long id;

	@Column(name = "uuid_person", updatable = false)
	private UUID uuidPerson;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name="sex",
			nullable = false)
	@Enumerated(value = EnumType.STRING)
	private Sex sex;

	@Column(name = "passport_series", nullable = false)
	private String passportSeries;

	@Column(name = "passport_number", nullable = false)
	private String passportNumber;

	@Column(name = "create_date", updatable = false)
	private LocalDateTime createDate;

	@Column(name = "update_date")
	private LocalDateTime updateDate;

	@Column(name = "deleted")
	private boolean deleted = Boolean.FALSE;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resident_house", nullable = false)
	@Fetch(FetchMode.JOIN)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private House residentHouse;

	@ManyToMany(mappedBy = "owners", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<House> ownHouses;

	public Person(Long id, UUID uuidPerson, String name, String surname, Sex sex, String passportSeries, String passportNumber, LocalDateTime createDate, LocalDateTime updateDate) {
		this.id = id;
		this.uuidPerson = uuidPerson;
		this.name = name;
		this.surname = surname;
		this.sex = sex;
		this.passportSeries = passportSeries;
		this.passportNumber = passportNumber;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}
}
