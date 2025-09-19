package com.SweetShopManagementSystem.service.service_impl;

import com.SweetShopManagementSystem.model.USER_ROLE;
import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerUserDetailsServiceImp {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findByEmail(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("user not found with email "+ username);
        }
        USER_ROLE role= user.get().getRole();
//        if(role==null){
//            role=USER_ROLE.ROLE_CUSTOMER;
//        }
        List<GrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
    }
}
