package io.proj3ct.bot.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;

@Entity(name = "usersDataTable")
public class User {

    @Id
    private  Long chatid;
    //@Column(name = "firstname")
    private String firstname;
   // @Column(name = "lastname")
    private String lastname;
   // @Column(name = "username")
    private String username;
   // @Column(name = "registeredat")
    private Timestamp registeredat;

    public Long getChatid() {
        return chatid;
    }

    public void setChatid(Long chatid) {
        this.chatid = chatid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getRegisteredat() {
        return registeredat;
    }

    public void setRegisteredat(Timestamp registeredat) {
        this.registeredat = registeredat;
    }

    @Override
    public String toString() {
        return "User{" +
                "ChatId=" + chatid +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", userName='" + username + '\'' +
                ", registeredAt=" + registeredat +
                '}';
    }
}
