package com.nothing.ecommerce.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.entity.Roles;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.repository.RolesRepository;
import com.nothing.ecommerce.services.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String reference) throws UsernameNotFoundException {
        // Fetch the user
        User user = userService.get(reference);

        // Verify that the user is Active
        if (!user.isActive()) {
            // throw new DisabledException("User not Active with Reference : " +
            // reference);
            user.setActive(1);
            userService.save(user);
            return loadUserByUsername(reference);
        }
        // Fetch User Roles
        List<Roles> roles = rolesRepository.findByUserId(user.getUserId());
        if (roles == null) {
            throw new BadCredentialsException("Role not found with Reference : " + reference);
        }
        List<String> roleNames = new ArrayList<String>();
        for (Roles role : roles) {
            roleNames.add(role.getRole());
        }

        // Generate Authority List
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roleNames.toArray(new String[0]));

        return new org.springframework.security.core.userdetails.User(reference, user.getPassword(),
                user.getActive() != 0,
                true, true, true, authorities);
    }

}
