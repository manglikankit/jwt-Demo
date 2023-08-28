package com.manglik.jwtdemo.Models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtRequest {
//    private String userName;
    private String password;
    private String email;
}
