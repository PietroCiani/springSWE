package com.example.springSWE.model;

import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="parks")
@Getter
@Setter
public class Park {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

	private String address;

	private LocalTime openingTime;

	private LocalTime closingTime;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", openingTime=" + openingTime +
				", closingTime=" + closingTime +
                '}';
    }
}
