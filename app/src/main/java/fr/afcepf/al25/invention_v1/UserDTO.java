package fr.afcepf.al25.invention_v1;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDTO implements Parcelable{
    private int idUser;
    private String prenom;
    private String nom;
    private int idCompte;
    private String email;
    private String password;
    private String role;

    public UserDTO() {}

    public UserDTO(int idUser, String prenom, String nom, int idCompte, String email, String password, String role) {
        this.idUser = idUser;
        this.prenom = prenom;
        this.nom = nom;
        this.idCompte = idCompte;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //parcel part
    public UserDTO(Parcel in){
        String[] data= new String[7];

        in.readStringArray(data);
        this.idUser = Integer.parseInt(data[0]);
        this.prenom = data[1];
        this.nom = data[2];
        this.idCompte = Integer.parseInt(data[3]);
        this.email = data[4];
        this.password = data[5];
        this.role = data[6];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(
                new String[]{
                        String.valueOf(this.idUser),
                        this.prenom,
                        this.nom,
                        String.valueOf(this.idCompte),
                        this.email,
                        this.password,
                        this.role
                });
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {

        @Override
        public UserDTO createFromParcel(Parcel source) {
            return new UserDTO(source);  //using parcelable constructor
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };
}
