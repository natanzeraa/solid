package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple;

public class SRPWrongExample {
    public void saveUser(String name, String email) {
        System.out.println("Salvando usuário: " + name + ", " + email);
    }

    public void sendEmail(String email, String message) {
        System.out.println("Enviando email para: " + email + " com a mensagem: " + message);
    }
}
