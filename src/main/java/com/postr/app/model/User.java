package com.postr.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.postr.app.common.PrimaryAudit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@JsonIgnoreProperties("hibernateLazyInitializer")
public class User extends PrimaryAudit {

  private String username;
}
