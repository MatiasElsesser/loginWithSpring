package com.example.demoJwt.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// lombok con estas anotaciones nos crea los geters, seters y toString, el constructor vacio y el que tiene los parametros
// con la notacion builder podemos crear objetos de la clase de manera mas sencilla y legible
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    String username;
    String password;
}
