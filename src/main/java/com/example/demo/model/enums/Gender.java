package com.example.demo.model.enums;

import lombok.Getter;

/** Enum that represents gender of the person.
 *
 */
@Getter
public enum Gender {
  MALE("Male"),
  FEMALE("Female");

  Gender(String string) {
  }

}
