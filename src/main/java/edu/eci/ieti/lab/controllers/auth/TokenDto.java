package edu.eci.ieti.lab.controllers.auth;

import java.util.Date;

public record TokenDto(String token, Date expirationDate) {

}
