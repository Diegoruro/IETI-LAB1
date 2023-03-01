package edu.eci.ieti.lab.controllers.auth;

import edu.eci.ieti.lab.exception.InvalidCredentialsException;
import edu.eci.ieti.lab.model.user.User;
import edu.eci.ieti.lab.security.encrypt.PasswordEncryptionService;
import edu.eci.ieti.lab.service.user.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
@RestController
@RequestMapping("v1/auth")
public class AuthController {

    int TOKEN_DURATION_MINUTES = 1440;
    private final UsersService usersService;

    private final PasswordEncryptionService passwordEncryptionService;

    @Value("${jwt.secret}")
    String secret;

    public AuthController(@Autowired UsersService usersService, @Autowired PasswordEncryptionService passwordEncryptionService) {
        this.usersService = usersService;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @PostMapping
    public TokenDto login(@RequestBody LoginDto loginDto) {
        Optional<User> optionalUser = usersService.findByEmail(loginDto.email());
        if (optionalUser.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User user = optionalUser.get();

        if (passwordEncryptionService.isPasswordMatch(loginDto.password(), user.getPasswordHash())) {
            return generateTokenDto(user);
        } else {
            throw new InvalidCredentialsException();
        }

    }


    private String generateToken(User user, Date expirationDate) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private TokenDto generateTokenDto(User user) {
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.MINUTE, TOKEN_DURATION_MINUTES);
        String token = generateToken(user, expirationDate.getTime());
        return new TokenDto(token, expirationDate.getTime());
    }
}
