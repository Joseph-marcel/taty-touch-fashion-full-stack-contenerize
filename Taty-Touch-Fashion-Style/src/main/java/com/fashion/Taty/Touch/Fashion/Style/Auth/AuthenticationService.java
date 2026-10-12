package com.fashion.Taty.Touch.Fashion.Style.Auth;


import com.fashion.Taty.Touch.Fashion.Style.Email.EmailService;
import com.fashion.Taty.Touch.Fashion.Style.Email.EmailTemplateName;
import com.fashion.Taty.Touch.Fashion.Style.Role.RoleRepository;
import com.fashion.Taty.Touch.Fashion.Style.User.Token;
import com.fashion.Taty.Touch.Fashion.Style.User.TokenRepository;
import com.fashion.Taty.Touch.Fashion.Style.User.User;
import com.fashion.Taty.Touch.Fashion.Style.User.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository  userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private String activationUrl = "http://localhost:4200/activate-account";


    public void register(RegistrationRequest request) throws MessagingException {

        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role User not initialized"));
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .accountEnabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {

        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVITE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {

        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().minusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {

        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom  secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
