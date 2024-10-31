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

	private LocalTime startTime;

	private LocalTime endTime;

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
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", duration='" + duration + '\'' +
				'}';
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		endTime = startTime.plusMinutes(duration);
		return endTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
		setEndTime(startTime.plusMinutes(duration));
	}

    public Reservation() {
    }

	public Reservation(LocalDate date, LocalTime startTime, Integer duration, User user, Park park) {
		this.date = date;
		this.startTime = startTime;
		this.endTime = startTime.plusMinutes(duration);
		this.duration = duration;
		this.user = user;
		this.park = park;
	}
}
