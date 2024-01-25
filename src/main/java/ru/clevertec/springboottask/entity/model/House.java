package ru.clevertec.springboottask.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "houses")
@SQLDelete(sql = "UPDATE houses SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class House {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uuid_house", updatable = false)
	private UUID uuidHouse;

	@Column(name = "area", nullable = false)
	private Double area;

	@Column(name = "country", nullable = false)
	private String country;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "street", nullable = false)
	private String street;

	@Column(name = "number", nullable = false)
	private Integer number;

	@Column(name = "create_date", updatable = false)
	private LocalDateTime createDate;

	@Column(name = "deleted")
	private boolean deleted = Boolean.FALSE;

	@OneToMany(mappedBy = "residentHouse", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Person> residents;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	@JoinTable(
			name = "owners",
			joinColumns = @JoinColumn(name = "house_id"),
			inverseJoinColumns = @JoinColumn(name = "person_id"))
	@JsonIgnore
	private List<Person> owners;

	public House(Long id, UUID uuidHouse, Double area, String country, String city, String street, Integer number, LocalDateTime createDate) {
		this.id = id;
		this.uuidHouse = uuidHouse;
		this.area = area;
		this.country = country;
		this.city = city;
		this.street = street;
		this.number = number;
		this.createDate = createDate;
	}

}
