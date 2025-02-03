package com.roomify.detection_be.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class HelperUtils {
    public static final ObjectWriter JSON_WRITER =
            new ObjectMapper().writer().withDefaultPrettyPrinter();
}
