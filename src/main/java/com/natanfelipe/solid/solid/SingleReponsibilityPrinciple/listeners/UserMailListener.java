package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.events.UserCreatedEvent;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Email;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Users;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.services.interfaces.IMail;

@Component
public class UserMailListener {
    private final IMail emailService;

    public UserMailListener(IMail emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        Users user = event.getUser();
        Email email = new Email();
        email.setSendTo(user.getEmail());
        email.setSendFrom("no-rely@natanfelipe.com");

        emailService.send(email);
    }
}
