package org.teamfour.system;
import org.teamfour.system.enums.Authority;
import java.util.HashMap;
import java.util.Map;
import org.teamfour.system.VotingSystem;
public class UserProfile {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Authority authority;

    public UserProfile(String username, String password, String firstName, String lastName, Authority authority) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authority = authority;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public Authority getAuthority() {
        return authority;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
    //
    private final Map<String, String> userMap = new HashMap<>();

    public boolean isUsernameUnique(String username) {
        return !userMap.containsKey(username);
    }

    public boolean isPasswordUnique(String password) {
        return userMap.values().stream().noneMatch(p -> p.equals(password));
    }

    public void addUser(String username, String password) {
        userMap.put(username, password);
    }
    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", authority=" + authority +
                '}';
    }

}
