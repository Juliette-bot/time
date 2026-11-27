package org.echangedetemps.user;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Gives the user who is making the request.
 *
 * TEMPORARY: there is no authentication yet, so it always returns the
 * demo user set in application.properties (app.temporary-author-email).
 * When login (JWT) is added, only this class will change:
 * services that call currentUser.get() will not need to be touched.
 */
@ApplicationScoped
public class CurrentUser {

    private final UserRepository userRepository;
    private final String temporaryAuthorEmail;

    public CurrentUser(UserRepository userRepository,
                       @ConfigProperty(name = "app.temporary-author-email") String temporaryAuthorEmail) {
        this.userRepository = userRepository;
        this.temporaryAuthorEmail = temporaryAuthorEmail;
    }

    public User get() {
        return userRepository.findByEmail(temporaryAuthorEmail)
                .orElseThrow(() -> new IllegalStateException(
                        "Temporary author not found: " + temporaryAuthorEmail
                                + " (are the demo data loaded?)"));
    }
}
