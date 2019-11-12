package android.example.com.newsolutionbodymanager.LoginAndFriend;

import com.google.firebase.firestore.Exclude;

public class User {
    public String name, email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
