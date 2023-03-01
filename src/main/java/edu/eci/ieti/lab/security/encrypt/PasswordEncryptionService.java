package edu.eci.ieti.lab.security.encrypt;

public interface PasswordEncryptionService {

    String encrypt(String password);


    boolean isPasswordMatch(String password, String encryptedPassword);


}
