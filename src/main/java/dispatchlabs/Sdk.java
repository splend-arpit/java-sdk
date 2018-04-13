package dispatchlabs;

import dispatchlabs.states.Account;
import dispatchlabs.states.Receipt;
import dispatchlabs.states.Contact;
import dispatchlabs.states.Transaction;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Http;
import org.json.JSONObject;

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
    private Receipt receipt;

    /**
     * @throws Exception
     */
    public Sdk(String seedNodeIp) throws Exception {
        this.seedNodeIp = seedNodeIp;
    }

    /**
     * @return
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<Contact> getDelegates() throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + seedNodeIp + ":1975/v1/delegates", getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return AJson.deserializeList(Contact.class, jsonObject.get("data").toString());
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public Account createAccount() throws Exception {
        return Account.create();
    }

    /**
     * @param contact
     * @param privateKey
     * @param from
     * @param to
     * @param tokens
     * @return
     * @throws Exception
     */
    public Receipt transferTokens(Contact contact, String privateKey, String from, String to, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(privateKey, from, to, Transaction.Type.TRANSFER_TOKENS, tokens, System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }

    /**
     * @param contact
     * @param fromAccount
     * @param toAccount
     * @param tokens
     * @return
     * @throws Exception
     */
    public Receipt transferTokens(Contact contact, Account fromAccount, Account toAccount, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), toAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, tokens, System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }

    /**
     * @param contact
     * @param address
     * @return
     * @throws Exception
     */
    public Account getAccount(Contact contact, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + contact.getEndpoint().getHost() + ":1975/v1/accounts/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return (Account) AJson.deserialize(Account.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param contact
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactions(Contact contact) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param contact
     * @param address
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactionsByFromAddress(Contact contact, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions/from/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @param contact
     * @param address
     * @return
     * @throws Exception
     */
    public List<Transaction> getTransactionsByToAddress(Contact contact, String address) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions/to/" + address, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            if (receipt.isOk()) {
                return AJson.deserializeList(Transaction.class, jsonObject.get("data").toString());
            }
            return null;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public Receipt getLastStatus() throws Exception {
        if (receipt == null) {
            return null;
        }
        return getStatus(receipt);
    }

    /**
     * @return
     * @throws Exception
     */
    public Receipt getStatus(Receipt receipt) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + receipt.getNodeIp() + ":1975/v1/statuses/" + receipt.getId(), getHeaders()));
            this.receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return this.receipt;
        }
    }

    /**
     * @param genesisAccount
     * @return
     * @throws Exception
     */
    public String createGenesisTransactionString(Account genesisAccount, long tokens) throws Exception {
        Account fromAccount = Account.create();
        Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), genesisAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, tokens, 0);
        return transaction.toString();
    }

    /**
     * @param contact
     * @param id
     * @return
     * @throws Exception
     */
    public Receipt getStatus(Contact contact, String id) throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + contact.getEndpoint().getHost() + ":1975/v1/statuses/" + id, getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
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
        System.out.println("Dispatch Labs SDK Example");
        try {
            Sdk sdk = new Sdk("10.0.1.2");
            List<Contact> contacts = sdk.getDelegates();
            Account fromAccount = sdk.createAccount();
            Account toAccount = sdk.createAccount();
            Receipt receipt = sdk.transferTokens(contacts.get(0), "e7181240095e27679bf38e8ad77d37bedb5865b569157b4c14cdb1bebb7c6e2b", "79db55dd1c8ae495c267bde617f7a9e5d5c67719", toAccount.getAddress(), 10000000);
            //Receipt receipt = sdk.transferTokens(contacts.get(0), fromAccount, toAccount, 45);
            System.out.println(receipt.getStatus());

            // Pending?
            while ((receipt = sdk.getLastStatus()).getStatus().equals(Receipt.Status.PENDING)) {
                Thread.sleep(100);
            }
            System.out.println(receipt.getStatus());

            // Get transactions.
            List<Transaction> transactions = sdk.getTransactions(contacts.get(0));
            System.out.println(transactions);
        } catch (Throwable t) {
            System.out.println(t);
        }
    }

}
