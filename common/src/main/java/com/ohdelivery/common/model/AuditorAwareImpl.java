package com.ohdelivery.common.model;

import com.ohdelivery.common.passport.usercontext.UserContextHolder;
import java.util.Optional;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(name = "org.springframework.data.domain.AuditorAware")
@ConditionalOnProperty(prefix = "app.jpa", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AuditorAwareImpl implements AuditorAware<String> {

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        String currentUser = UserContextHolder.getPassport().getUserId();
        return (currentUser != null && !currentUser.isEmpty())
            ? Optional.of(currentUser)
            : Optional.of("system");
    }
}
