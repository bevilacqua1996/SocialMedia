package com.social.media;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API Social Media",
                version = "1.0",
                contact = @Contact(
                        name = "Bruno Nascimento",
                        url = "https://bio.link/bevilacqua96",
                        email = "brunoenig@hotmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                )
        )
)
public class SocialMediaApplication extends Application {
}
