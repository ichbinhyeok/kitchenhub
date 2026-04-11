package owner.kitchencompliance.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminSecurityProperties(
        String username,
        String password
) {

    public AdminSecurityProperties {
        username = username == null || username.isBlank() ? "admin" : username;
        password = password == null || password.isBlank() ? "tlsgur3108" : password;
    }
}
