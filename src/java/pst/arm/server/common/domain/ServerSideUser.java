package pst.arm.server.common.domain;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import pst.arm.client.common.domain.User;

/**
 * server side wrapper only. does NOT extend User
 * 
 * @author Alexandr Kozhin
 */
public class ServerSideUser implements UserDetails {

    private static final Logger log = Logger.getLogger(ServerSideUser.class);
    private User user;

    public ServerSideUser(User u) {
        this.user = u;
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        List<String> facilities = user.getFacilities();
        GrantedAuthority[] auths = new GrantedAuthority[facilities.size()];
        int i=0;
        for(String facility : facilities){
            auths[i] = new UserAuthority(facility);
            i++;
        }
        return auths;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private class UserAuthority implements GrantedAuthority {

        private String auth;

        public UserAuthority(String auth) {
            this.auth = auth;
        }

        @Override
        public String getAuthority() {
            return auth;
        }

        @Override
        public int compareTo(Object o) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired();
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return user.getId();
    }

    public Date getDateCreated() {
        return user.getDateCreated();
    }
}
