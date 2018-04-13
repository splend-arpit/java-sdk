package dispatchlabs;

import dispatchlabs.crypto.Crypto;
import dispatchlabs.states.Account;
import dispatchlabs.states.Action;
import dispatchlabs.states.Contact;
import dispatchlabs.states.Transaction;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Http;
import dispatchlabs.utils.Utils;
import org.bitcoin.NativeSecp256k1;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Sdk {

    /**
     * Class level-declarations.
     */
    private String seedNodeIp;

    /**
     * @throws Exception
     */
    public Sdk(String seedNodeIp) throws Exception {
        this.seedNodeIp = seedNodeIp;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<Contact> getDelegates() throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + seedNodeIp + ":1975/v1/delegates", getHeaders()));
            return AJson.deserializeList(Contact.class, jsonObject.get("data").toString());
        }
    }

    /**
     * @param contact
     * @param privateKey
     * @param from
     * @param to
     * @param tokens
     * @throws Exception
     */
    public Action transferTokens(Contact contact, String privateKey, String from, String to, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(privateKey, from, to, Transaction.Type.TRANSFER_TOKENS, tokens);
            String response = http.post("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders(), transaction.toString());

            int fook=0;
        }

        return null;
    }

    /**
     * @return @throws Exception
     */
    private Map<String, String> getHeaders() throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * @param args
     */
    public static void main(String args[]) {
        try {
            /*
            Account fromAccount = Account.create();
            Account toAccount = Account.create();
            */

            Sdk sdk = new Sdk("10.0.1.2");

            List<Contact> contacts = sdk.getDelegates();

            sdk.transferTokens(contacts.get(0), "e7181240095e27679bf38e8ad77d37bedb5865b569157b4c14cdb1bebb7c6e2b", "79db55dd1c8ae495c267bde617f7a9e5d5c67719", "43f603c04610c87326e88fcd24152406d23da032", 45);

            int fook = 0;

        } catch (Throwable t) {
            int fook = 0;
        }
    }

}
