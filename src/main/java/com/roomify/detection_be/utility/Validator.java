package com.roomify.detection_be.utility;

public class Validator {

  public static boolean isValidUUID(String uuid) {
    return uuid.matches("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$");
  }
}
