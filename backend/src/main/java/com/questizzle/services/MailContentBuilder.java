package com.questizzle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Created by Danny on 4/7/2017.
 */
@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildConfirmationEmail(String confirmationLink) {
        Context context = new Context();
        context.setVariable("confirmationLink", confirmationLink);
        return templateEngine.process("mailTemplate", context);
    }

}
