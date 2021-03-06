package pl.raporty.models;


import org.mindrot.jbcrypt.BCrypt;

public class User {

    private int id;
    private String name;
    private String password;

    public User () {}

    public User (String name, String password) {
        this.name = name;
        this.hashPassword(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void hashPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
