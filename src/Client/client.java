package Client;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import Mvc.ClientMvc.*;

import connectionProtocol.*;
import connectionProtocol.Package;

public class Client {
    private String username;
    private String password;
    private String name;
    private String email;
    private LocalDate birth_date;

    public Client() {
        this.username = "";
        this.password = "";
        this.name = "";
        this.email = "";
        this.birth_date = null;
    }

    public Client(String username, String password,String name ,String email, LocalDate birth_date) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
    }

    public Client(Client c) {
        this.username = c.getUsername();
        this.password = c.getPassword();
        this.name = c.getName();
        this.email = c.getEmail();
        this.birth_date = c.getBirth_date();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
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

    public static Client deserialize(DataInputStream in) {
        try {
            String username = in.readUTF();
            String password = in.readUTF();
            String name = in.readUTF();
            String email = in.readUTF();
            String birthDateString = in.readUTF();
            LocalDate birth_date = LocalDate.parse(birthDateString);
    
            return new Client(username,password,name,email, birth_date);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Client authenticate(String username, String password, Connection connection) throws Exception {
        Package snd = new Login(username, password);
        byte[] sndBytes = snd.convertPackageToBytes();
        connection.send(sndBytes);

        byte[] response = connection.read();
        LoginResponse responsePkg = LoginResponse.convertBytesToPackage(response);

        if(responsePkg.isSuccess()){
            return responsePkg.getClient();
        }
        return null;
    }

    public boolean register(String username, String password, String name , String email, String birth_date , boolean b, Connection connection) throws Exception {
        Package snd = new Registo(username, password, name,email,birth_date, b);
        byte[] sndBytes = snd.convertPackageToBytes();
        connection.send(sndBytes);

        byte[] response = connection.read();
        RegisterResponse responsePkg = RegisterResponse.convertBytesToPackage(response);
        return responsePkg.isSuccess();
    }

    public void serialize(DataOutputStream out) throws IOException {
        out.writeUTF(this.getUsername());
        out.writeUTF(this.getPassword());
        out.writeUTF(this.getName());
        out.writeUTF(this.getEmail());
        out.writeUTF(this.getBirth_date() != null ? this.getBirth_date().toString() : "");
    }

    public static void main(String[] args) {
        try {
            MainController controller = new MainController();
            controller.start();

        } catch (Exception e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
      
}
