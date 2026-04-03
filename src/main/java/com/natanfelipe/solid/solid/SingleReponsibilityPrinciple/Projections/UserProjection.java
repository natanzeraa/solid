package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Projections;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Models.User;

public interface UserProjection {
    String findFullNameById();
    String findByEmail();
    String findByPhone();

    User createUser();
}
