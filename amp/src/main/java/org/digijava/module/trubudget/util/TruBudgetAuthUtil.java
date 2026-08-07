package org.digijava.module.trubudget.util;

import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.cache.ehcache.EhCacheWrapper;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.aim.dbentity.AmpGlobalSettings;
import org.digijava.module.um.util.DbUtil;
import org.digijava.module.um.model.TruLoginRequest;
import org.digijava.module.um.model.TruLoginResponse;
import org.digijava.module.um.util.UmUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.digijava.module.um.util.DbUtil.getGlobalSettingsBySection;
import static org.digijava.module.um.util.DbUtil.getSettingValue;
import static org.digijava.module.um.util.DbUtil.loginToTruBudget;

/**
 * Utility class for TruBudget authentication and token management
 */
public class TruBudgetAuthUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(TruBudgetAuthUtil.class);

    public static class TruBudgetLoginAttemptResult {
        private final boolean attempted;
        private final boolean success;
        private final String message;

        public TruBudgetLoginAttemptResult(boolean attempted, boolean success, String message) {
            this.attempted = attempted;
            this.success = success;
            this.message = message;
        }

        public boolean isAttempted() {
            return attempted;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
    
    /**
     * Extracts token from response cookies and sets it in the TruLoginResponse object
     * 
     * @param responseBody The TruLoginResponse object to populate with token
     * @param headers The HTTP response headers containing Set-Cookie headers
     */
    public static void extractTokensFromCookies(TruLoginResponse responseBody, HttpHeaders headers) {
        if (responseBody == null || headers == null) {
            return;
        }
        
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        if (cookies == null || cookies.isEmpty()) {
            return;
        }
        
        if (responseBody.getData() == null || responseBody.getData().getUser() == null) {
            return;
        }
        
        for (String cookie : cookies) {
            // Look for token in cookie (common patterns: "token=...", "auth-token=...", etc.)
            // Exclude refreshToken to avoid matching it here
            if (cookie.contains("token=") && !cookie.contains("refreshToken=")) {
                String tokenValue = extractTokenFromCookie(cookie, "token");
                if (tokenValue != null) {
                    responseBody.getData().getUser().setToken(tokenValue);
                    logger.info("Token extracted from cookie and set in response");
                }
            }
        }
    }
    
    /**
     * Extracts token value from a cookie string
     * 
     * @param cookie The cookie string (e.g., "token=value; Path=/; HttpOnly")
     * @param cookieName The name of the cookie to extract (e.g., "token", "refreshToken")
     * @return The token value, or null if not found
     */
    private static String extractTokenFromCookie(String cookie, String cookieName) {
        // Extract token value from cookie string
        // Format: "token=value; Path=/; HttpOnly" or similar
        if (cookie == null || cookie.isEmpty()) {
            return null;
        }
        
        // Look for the specified cookie name
        int tokenStart = cookie.indexOf(cookieName + "=");
        if (tokenStart == -1 && "token".equals(cookieName)) {
            // Try other common cookie names for token
            tokenStart = cookie.indexOf("auth-token=");
            if (tokenStart == -1) {
                tokenStart = cookie.indexOf("authToken=");
            }
        }
        
        if (tokenStart != -1) {
            int valueStart = cookie.indexOf("=", tokenStart) + 1;
            int valueEnd = cookie.indexOf(";", valueStart);
            if (valueEnd == -1) {
                valueEnd = cookie.length();
            }
            return cookie.substring(valueStart, valueEnd).trim();
        }
        
        return null;
    }
    
    /**
     * Caches tokens and user information from a TruLoginResponse
     * 
     * @param truLoginResponse The login response containing tokens
     * @param userEmail The user's email address (used for caching user info)
     */
    public static void cacheTokensFromResponse(TruLoginResponse truLoginResponse, String userEmail) {
        if (truLoginResponse == null || truLoginResponse.getData() == null 
                || truLoginResponse.getData().getUser() == null) {
            logger.warn("Cannot cache tokens: TruLoginResponse is null or incomplete");
            return;
        }
        
        AbstractCache myCache = new EhCacheWrapper("trubudget");
        
        // Cache the access token
        String token = truLoginResponse.getData().getUser().getToken();
        if (token != null && !token.isEmpty()) {
            myCache.put("truBudgetToken", token);
            logger.info("TruBudget access token cached successfully");
        } else {
            logger.warn("TruBudget access token is null or empty - token may not have been extracted from cookies");
        }
        
        
        // Cache user information
        if (userEmail != null && !userEmail.isEmpty()) {
            User cachedUser = UserUtils.getUserByEmailAddress(userEmail);
            myCache.put("truBudgetUser", cachedUser != null && cachedUser.getTruBudgetUserName() != null
                    ? cachedUser.getTruBudgetUserName()
                    : DbUtil.getDefaultTruBudgetUserName(userEmail));
            myCache.put("truBudgetPassword", userEmail);
        }
    }
    
    /**
     * Re-logs in to TruBudget using cached user email and returns new token
     * This is used when the token expires and needs to be refreshed
     * 
     * @return The new access token, or null if re-login fails
     */
    public static Mono<String> reLoginToTruBudget() {
        try {
            AbstractCache cache = new EhCacheWrapper("trubudget");
            String userEmail = (String) cache.get("truBudgetPassword"); // This stores the email
            
            if (userEmail == null || userEmail.isEmpty()) {
                logger.warn("Cannot re-login: user email not found in cache");
                return Mono.empty();
            }
            
            // Get user from database
            User user = UserUtils.getUserByEmailAddress(userEmail);
            if (user == null || !user.getTruBudgetEnabled() || user.getTruBudgetPassword() == null) {
                logger.warn("Cannot re-login: user not found or TruBudget not enabled for user: " + userEmail);
                return Mono.empty();
            }
            
            // Perform login
            List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
            TruLoginRequest truLoginRequest = new TruLoginRequest();
            truLoginRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
            TruLoginRequest.Data data = new TruLoginRequest.Data();
            TruLoginRequest.User user1 = new TruLoginRequest.User();
            user1.setPassword(UmUtil.decryptTruBudgetPassword(user.getTruBudgetPassword(), user.getEmail(), user.getTruBudgetKeyGen()));
            user1.setId(DbUtil.resolveTruBudgetUserName(user.getTruBudgetUserName(), user.getEmail()));
            data.setUser(user1);
            truLoginRequest.setData(data);
            
            return loginToTruBudget(truLoginRequest, settings)
                    .doOnSuccess(truLoginResponse -> {
                        // Cache the new tokens
                        cacheTokensFromResponse(truLoginResponse, userEmail);
                        logger.info("TruBudget re-login successful and tokens cached");
                    })
                    .map(truLoginResponse -> {
                        if (truLoginResponse.getData() != null && truLoginResponse.getData().getUser() != null) {
                            return truLoginResponse.getData().getUser().getToken();
                        }
                        return null;
                    })
                    .onErrorResume(e -> {
                        logger.error("Failed to re-login to TruBudget: " + e.getMessage(), e);
                        return Mono.empty();
                    });
        } catch (Exception e) {
            logger.error("Error attempting TruBudget re-login: " + e.getMessage(), e);
            return Mono.empty();
        }
    }
    
    /**
     * Performs TruBudget login for the given user and caches the tokens
     * 
     * @param currentUser The user to login to TruBudget
     * @throws Exception if login fails
     */
    public static void doActualTruBudgetLogin(User currentUser) throws Exception {
        doActualTruBudgetLoginWithResult(currentUser);
    }

    public static TruBudgetLoginAttemptResult doActualTruBudgetLoginWithResult(User currentUser) {
        List<AmpGlobalSettings> settings = getGlobalSettingsBySection("trubudget");
        if (currentUser == null) {
            logger.info("Skipping TruBudget login because current user is null");
            return new TruBudgetLoginAttemptResult(false, false, null);
        }

        if (getSettingValue(settings,"isEnabled").equalsIgnoreCase("true") && currentUser.getTruBudgetEnabled() && currentUser.getTruBudgetPassword()!=null) {
            logger.info("Attempting TruBudget login for user: {}", currentUser.getEmail());

            try {
                // login into TruBudget
                TruLoginRequest truLoginRequest = new TruLoginRequest();
                truLoginRequest.setApiVersion(getSettingValue(settings, "apiVersion"));
                TruLoginRequest.Data data = new TruLoginRequest.Data();
                TruLoginRequest.User user1 = new TruLoginRequest.User();
                user1.setPassword(UmUtil.decryptTruBudgetPassword(currentUser.getTruBudgetPassword(), currentUser.getEmail(), currentUser.getTruBudgetKeyGen()));
                user1.setId(DbUtil.resolveTruBudgetUserName(currentUser.getTruBudgetUserName(), currentUser.getEmail()));
                data.setUser(user1);
                truLoginRequest.setData(data);
                Mono<TruLoginResponse> truResp = loginToTruBudget(truLoginRequest, settings);
                TruLoginResponse loginResponse = truResp.block();

                if (loginResponse == null) {
                    logger.warn("TruBudget login completed with no response for user: {}", currentUser.getEmail());
                    return new TruBudgetLoginAttemptResult(true, false,
                            "TruBudget login failed. You can continue using AMP.");
                }

                logger.info("TruBudget login success for user: {}", currentUser.getEmail());
                if (loginResponse.getData() != null) {
                    logger.info("TruBudget login response payload: {}", loginResponse.getData());
                }
                cacheTokensFromResponse(loginResponse, currentUser.getEmail());
                return new TruBudgetLoginAttemptResult(true, true,
                        "TruBudget login successful.");
            } catch (Exception e) {
                logger.error("Error during TruBudget login for user {}: {}", currentUser.getEmail(), e.getMessage(), e);
                return new TruBudgetLoginAttemptResult(true, false,
                        "TruBudget login failed. You can continue using AMP.");
            }
        } else {
            logger.info("Skipping TruBudget login for user {}. Enabled={}, userEnabled={}, hasPassword={}",
                    currentUser != null ? currentUser.getEmail() : "null",
                    getSettingValue(settings, "isEnabled"),
                    currentUser != null && currentUser.getTruBudgetEnabled(),
                    currentUser != null && currentUser.getTruBudgetPassword() != null);
            return new TruBudgetLoginAttemptResult(false, false, null);
        }
    }
}
