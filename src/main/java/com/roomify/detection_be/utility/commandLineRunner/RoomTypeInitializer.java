package com.roomify.detection_be.utility.commandLineRunner;

import com.roomify.detection_be.repository.RoleRepository;
import com.roomify.detection_be.repository.RoomTypeRepository;
import com.roomify.detection_be.web.entities.Role;
import com.roomify.detection_be.web.entities.RoomType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class RoomTypeInitializer implements CommandLineRunner {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeInitializer(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<String> defaultTypes = List.of("DEFAULT", "VIP");

        for (String typeName : defaultTypes) {
            if (!roomTypeRepository.existsByName(typeName)) {
                RoomType roomType = new RoomType();
                roomType.setName(typeName);
                if(typeName.equals("DEFAULT")){
                    roomType.setDescription("Default room type");
                    roomType.setMaxCapacity(10);
                }
                else{
                    roomType.setDescription("VIP room type");
                    roomType.setMaxCapacity(20);
                }
                roomTypeRepository.save(roomType);
            }
        }
    }
}
