package com.example.fuelprices.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.fuelprices.model.User;
import com.example.fuelprices.model.UserRole;
import com.example.fuelprices.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        FirebaseToken decodedToken = null;
        String authHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            try {
                decodedToken = FirebaseAuth.getInstance().verifyIdToken(authHeader.substring(7));
            } catch (FirebaseAuthException e) {
                log.error("Firebase auth exception: ", e.getMessage());
            }
        }

        if(decodedToken != null) {

            String firebaseId = decodedToken.getUid();
            User currentUser = userRepository.findByFirebaseId(firebaseId)
                                            .orElseGet(() -> {
                                                User firstTime = new User();
                                                firstTime.setFirebaseId(firebaseId);
                                                return userRepository.save(firstTime);
                                            });

            if(currentUser != null ) {

                currentUser.setEmailConfirmed(decodedToken.isEmailVerified());
                currentUser.setEmail(decodedToken.getEmail());
                currentUser.setName(decodedToken.getName());

                currentUser.setRole(UserRole.NEW);

                if(currentUser.isEmailConfirmed())
                    currentUser.setRole(UserRole.USER);

                if(decodedToken.getClaims().containsKey("moderator") 
                    && decodedToken.getClaims().get("moderator") instanceof Boolean mod && mod)
                        currentUser.setRole(UserRole.MODERATOR);

                if(decodedToken.getClaims().containsKey("admin") 
                    && decodedToken.getClaims().get("admin") instanceof Boolean admin && admin)
                        currentUser.setRole(UserRole.ADMIN);

                if(decodedToken.getClaims().containsKey("locked")
                    && decodedToken.getClaims().get("locked") instanceof Boolean locked)
                        currentUser.setLocked(locked);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    currentUser, null, currentUser.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }

        filterChain.doFilter(request, response);
    }
    
}
