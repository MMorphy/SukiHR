package hr.petkovic.iehr.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hr.petkovic.iehr.entity.User;
import hr.petkovic.iehr.repo.UserRepo;

@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(UserService.class);

	private UserRepo userRepo;

	public UserService(UserRepo userR) {
		userRepo = userR;
	}

	public User findUserByUsername(String username) {
		try {
			return this.userRepo.findByUsername(username).get();
		} catch (Exception e) {
			return null;
		}
	}

	public Float getSummarySaldo() {
		Float sumSaldo = 0.0f;
		List<User> users = findAllEnabledUsers();
		for (User u : users) {
			sumSaldo += u.getSaldo();
		}
		return sumSaldo;
	}

	public Float getSaldoForLoggedInUser() {
		for (GrantedAuthority ga : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (ga.toString().equals("ROLE_ADMIN")) {
				return getSummarySaldo();
			}
		}
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		if (username == null || findUserByUsername(username) == null || findUserByUsername(username).getSaldo() == null)
			return new Float(0);
		else
			return findUserByUsername(username).getSaldo();
	}

	public Float updateSaldoAndSave(Float amount, boolean increase) {
		User u = findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if (increase) {
			u.increaseSaldo(amount);
		} else {
			u.decreaseSaldo(amount);
		}
		return saveUser(u).getSaldo();
	}

	public User findUserById(Long id) {
		try {
			return this.userRepo.findById(id).get();
		} catch (Exception ex) {
			logger.error("Exception occured in findUserById", ex);
			return null;
		}
	}

	public List<User> findAllEnabledUsers() {
		return userRepo.findByEnabled(true);
	}

	public User disableUser(Long id) {
		Optional<User> optU = userRepo.findById(id);
		if (optU.isPresent()) {
			User u = optU.get();
			u.setEnabled(false);
			return userRepo.save(u);
		} else
			return null;
	}

	public User updateUser(Long id, User user) {
		Optional<User> optU = userRepo.findById(id);
		if (optU.isPresent()) {
			User u = optU.get();
			u.setEnabled(user.isEnabled());
			u.setPassword(user.getPassword());
			u.setUsername(user.getUsername());
			u.setRoles(user.getRoles());
			return userRepo.save(u);
		} else
			return userRepo.save(user);
	}

	public User saveUser(User user) {
		return userRepo.save(user);
	}

	public User getBankUser() {
		return findUserByUsername("banka");
	}

}
