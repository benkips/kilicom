package com.mabnets.kilicom;

public class details {
    private String username;
    private String phone;
    private String adress;
    private String email;


    public details(String username, String phone, String adress, String email) {
        this.username = username;
        this.phone = phone;
        this.adress = adress;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }

    public String getEmail() {
        return email;
    }
}
