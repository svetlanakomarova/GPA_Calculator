package ca.sheridancollege.komarovs.assignment3.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.komarovs.assignment3.database.DatabaseAccess;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private DatabaseAccess da;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws 
		UsernameNotFoundException {
		
		// retrieves user by username
		ca.sheridancollege.komarovs.assignment3.beans.User user = 
				da.findUserAccount(username);
		
		// in case the user doesn't exist display a message
		if (user == null) {
			System.out.printf("User not found: %s%n", username);
			throw new UsernameNotFoundException("User " + username 
					+ " not found in database.");
		} 
		
		// retrieves list of roles for specific user
		List<String> rolesList = da.getRolesById(user.getUserId());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		
		if (rolesList != null) {		
			for (String role : rolesList)
				grantList.add(new SimpleGrantedAuthority(role));	
		}
		
		UserDetails userDetails = (UserDetails)new User(user.getUsername(),
				user.getEncryptedPassword(), grantList);
		
		return userDetails;
	}

}
