package com.example.inventorsoft_homework5.service.impl;

import com.example.inventorsoft_homework5.entity.User;
import com.example.inventorsoft_homework5.exception.NoSuchUserException;
import com.example.inventorsoft_homework5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NoSuchUserException("User not found with login: " + login));
        return UserDetailsImpl.build(user);
    }

}
