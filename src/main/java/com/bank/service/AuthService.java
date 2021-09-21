package com.bank.service;

import com.bank.entity.Customer;
import com.bank.configuration.CustomUserDetails;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    CustomerService customerService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Customer customer = customerService.getByUserName(s);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(1);
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        return new CustomUserDetails(
                customer.getLogin(),
                customer.getPasswordHash(),
                grantedAuthorities,
                customer.getId());
    }
}
