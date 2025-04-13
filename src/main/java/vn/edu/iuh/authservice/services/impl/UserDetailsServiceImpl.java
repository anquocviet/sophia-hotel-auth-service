package vn.edu.iuh.authservice.services.impl;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.authservice.models.User;
import vn.edu.iuh.authservice.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   public UserDetailsServiceImpl(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      User user = userRepository.findByUsernameLikeIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

      return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().name())
      );
   }
}
