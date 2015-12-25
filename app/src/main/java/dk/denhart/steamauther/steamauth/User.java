package dk.denhart.steamauther.steamauth;
import java.security.NoSuchAlgorithmException;

/**
 * Created by DenPC on 22-12-2015.
 */

    /*
    * User
    *   account_name
    *   shared_secret
    *   identity_secret
    *   steamCode
    * */
public class User {
    private String accountName;
    private String sharedSecret;
    private String identitySecret;
    private int    id;

    public User(){

    }

    public User(int id, String accountName, String sharedSecret, String identitySecret) {
        this.id = id;
        this.accountName = accountName;
        this.sharedSecret = sharedSecret;
        this.identitySecret = identitySecret;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getaccountName() {
        return accountName;
    }

    public void setaccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getsharedSecret() {
        return sharedSecret;
    }

    public void setsharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String getidentitySecret() {
        return identitySecret;
    }

    public void setidentitySecret(String identitySecret) {
        this.identitySecret = identitySecret;
    }

    public String getsteamCode() {
        SteamTOTP steam = new SteamTOTP(sharedSecret, identitySecret ,0);
        try {
            return steam.getAuthCode();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}

