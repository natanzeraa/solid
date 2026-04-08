package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Email;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.services.interfaces.IMail;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;

@Service
public class EmailService implements IMail {
	
	@Value("${mailtrap.token}")
	private String TOKEN;

	@Value("${mailtrap.sender}")
	private String SENDER;

	
	public void send(Email email) {
		System.out.println("Token: " + TOKEN);
		System.out.println("Sender: " + SENDER);

        final MailtrapConfig config = new MailtrapConfig.Builder()
            .token(TOKEN)
            .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
            .from(new Address(SENDER))
            .to(List.of(new Address(email.getSendTo())))
            .subject("Hello from Java SRP (Single Responsibility Principle)!")
            .text("This email was sent using Mailtrap's Java SDK.")
            .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }
    }
}
 