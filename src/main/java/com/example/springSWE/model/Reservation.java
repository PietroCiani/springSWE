package com.example.springSWE.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="reservations")
@Getter
@Setter
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;

	private LocalTime time;

	private Integer duration;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "park_id", referencedColumnName = "id")
	private Park park;

	@Override
	public String toString() {
		return "Reservation{" +
				"id=" + id +
				", date='" + date + '\'' +
				", startTime='" + time + '\'' +
				", duration='" + duration + '\'' +
				'}';
	}

	public LocalTime getStartTime() {
		return time;
	}

	public LocalTime getEndTime() {
		return time.plusMinutes(duration);
	}
}
