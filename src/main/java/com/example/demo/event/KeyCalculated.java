package com.example.demo.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyCalculated {
    private String username;
    private String roomId;
    private String otherKey;
    private String secretKey;
}
