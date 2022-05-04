package com.sesac.foodtruckitem.infrastructure.persistence.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "map")
@Entity
public class Map extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long id;
    private Double latitude;
    private Double longitude;

    public static Map of(Double latitude, Double longitude) {
        Map map = Map.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return map;
    }
}
