package Client;

import java.time.LocalDate;

public class Admin extends Client{

    public Admin(){
        super();
    }    

    public Admin(String username, String password, String email, LocalDate birth_date)
    {
        super(username, password, email, birth_date);
    }

    public Admin(Admin a)
    {
        super(a.getUsername(), a.getPassword(), a.getEmail(), a.getBirth_date());
    }
}