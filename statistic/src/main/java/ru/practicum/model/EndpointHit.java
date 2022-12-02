package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "endpoint_hit", schema = "public")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endpoint_hit_id")
    private Long id;

    @Column(name = "app", nullable = false, length = 150)
    private String app;

    @Column(name = "uri", nullable = false, length = 150)
    private String uri;

    @Column(name = "ip", nullable = false, length = 30)
    private String ip;
    private LocalDateTime timestamp;
}
