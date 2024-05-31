package ru.bank.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_seq_gen")
    @SequenceGenerator(name = "base_seq_gen", sequenceName = "base_seq", allocationSize = 1)
    @Column(unique = true)
    public int id;

    @JsonIgnore
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false)
    private Date updatedAt;

}