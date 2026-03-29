package com.app.quantitymeasurement.security.oauth2;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.quantitymeasurement.dto.request.RegisterRequestDTO;
import com.app.quantitymeasurement.model.User;
import com.app.quantitymeasurement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public User register(RegisterRequestDTO req) {
		
		if(userRepo.findByEmail (req.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        return userRepo.save(user);

		
	}
	 // ── Find user by email ────────────────────────────────────────────────────
    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
