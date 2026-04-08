package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.events;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Users;

public class UserCreatedEvent {
    private final Users user;

    public UserCreatedEvent(Users user) {
        this.user = user;
    }

    public Users getUser(){
        return user;
    }
}
