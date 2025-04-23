package com.ohdelivery.common.model;

import com.ohdelivery.common.passport.Passport;
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
        Passport passport = UserContextHolder.getPassport();
        return (passport != null && passport.getUserId() != null && !passport.getUserId().isEmpty())
            ? Optional.of(passport.getUserId())
            : Optional.of("system");
    }
}
