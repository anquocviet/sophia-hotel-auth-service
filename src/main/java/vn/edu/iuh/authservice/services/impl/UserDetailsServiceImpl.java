package vn.edu.iuh.authservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.authservice.exceptions.impl.UserNotFoundException;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;

/**
 * @description UserDetailsService implementation for Spring Security
 * @author: vie
 * @date: 14/2/25
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      log.debug("Loading user details for username: {}", username);
      
      User user = userRepository.findByUsernameLikeIgnoreCase(username)
            .orElseThrow(() -> {
               log.warn("User not found during authentication: {}", username);
               return new UsernameNotFoundException("User not found with username: " + username);
            });

      log.debug("User found for authentication: {}", username);
      return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().name())
      );
   }
}
