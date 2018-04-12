package dispatchlabs.states;

import dispatchlabs.crypto.Crypto;
import dispatchlabs.crypto.Key;
import dispatchlabs.utils.Utils;

import java.sql.Timestamp;

/**
 *
 */
public class Account {

    /**
     * Class level-declarations.
     */
    private String privateKey;
    private String address;
    private String name;
    private long balance;
    private Timestamp updated;
    private Timestamp created;

    /**
     *
     * @return
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     *
     * @param privateKey
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public long getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     */
    public void setBalance(long balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     */
    public Timestamp getUpdated() {
        return updated;
    }

    /**
     *
     * @param updated
     */
    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    /**
     *
     * @return
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     *
     * @param created
     */
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public static Account create() throws Exception {
        Key key = new Key();
        byte[] publicKey = key.getPublicKeyBytes();
        byte[] hashablePublicKey = new byte[32];
        for (int i=1; i<publicKey.length; i++) {
            hashablePublicKey[i-1] = publicKey[i];
        }
        byte[] hash = Crypto.hash(hashablePublicKey);
        byte[] address = new byte[20];
        for (int i=0; i<address.length; i++) {
            address[i] = hash[i+12];
        }
        Account account = new Account();
        account.setAddress(Utils.toHexString(address));
        account.setPrivateKey(key.getPrivateKey());
        return account;
    }
}



