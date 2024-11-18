package Client;

import java.time.LocalDate;

public class client {
    private String username;
    private String password;
    private String email;
    private LocalDate birth_date;


    public client() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.birth_date = null;
    }

    public client(String username, String password, String email, LocalDate birth_date) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birth_date = birth_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }
}
