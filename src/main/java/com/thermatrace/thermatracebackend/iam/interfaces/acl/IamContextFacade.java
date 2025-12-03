package com.thermatrace.thermatracebackend.iam.interfaces.acl;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByIdQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * IamContextFacade
 * Anti-Corruption Layer for IAM bounded context
 * Allows other bounded contexts to interact with IAM without tight coupling
 */
@Service
public class IamContextFacade {

    private final UserQueryService userQueryService;

    public IamContextFacade(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    /**
     * Get user by ID
     * @param userId the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> getUserById(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId));
    }

    /**
     * Get user by email
     * @param email the user email
     * @return Optional containing the user if found
     */
    public Optional<User> getUserByEmail(String email) {
        return userQueryService.handle(new GetUserByEmailQuery(email));
    }

    /**
     * Check if user can add medicament based on their plan
     * @param userId the user ID
     * @param currentMedicamentCount current number of medicaments
     * @return true if user can add medicament, false otherwise
     */
    public boolean canUserAddMedicament(Long userId, int currentMedicamentCount) {
        return getUserById(userId)
                .map(user -> user.canAddMedicament(currentMedicamentCount))
                .orElse(false);
    }

    /**
     * Get user's medicament limit based on their plan
     * @param userId the user ID
     * @return medicament limit or 0 if user not found
     */
    public int getUserMedicamentLimit(Long userId) {
        return getUserById(userId)
                .map(User::getMedicamentLimit)
                .orElse(0);
    }

    /**
     * Check if user has daily alerts enabled based on their plan
     * @param userId the user ID
     * @return true if user has daily alerts, false otherwise
     */
    public boolean userHasDailyAlerts(Long userId) {
        return getUserById(userId)
                .map(User::hasDailyAlerts)
                .orElse(false);
    }

    /**
     * Check if user has advanced reports enabled based on their plan
     * @param userId the user ID
     * @return true if user has advanced reports, false otherwise
     */
    public boolean userHasAdvancedReports(Long userId) {
        return getUserById(userId)
                .map(User::hasAdvancedReports)
                .orElse(false);
    }
}
