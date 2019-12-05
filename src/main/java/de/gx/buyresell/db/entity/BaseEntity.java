package de.gx.buyresell.db.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
@Data
class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
}
