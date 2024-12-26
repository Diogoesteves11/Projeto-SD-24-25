package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

public class Admin extends Client{


    public Admin(){
        super();
    }    

    public Admin(String username, String password,String name ,String email, LocalDate birth_date)
    {
        super(username, password,name ,email, birth_date);
    }

    public Admin(Admin a)
    {
        super(a.getUsername(), a.getPassword(),a.getName() ,a.getEmail(), a.getBirth_date());
    }

}