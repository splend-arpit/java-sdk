package dispatchlabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import dispatchlabs.states.Account;
import dispatchlabs.states.Node;
import dispatchlabs.states.Receipt;
import dispatchlabs.states.Transaction;
import dispatchlabs.utils.AJson;
import dispatchlabs.utils.Http;


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
    public List<Node> getDelegates() throws Exception {
        try (Http http = new Http()) {
            JSONObject jsonObject = new JSONObject(http.get("http://" + seedNodeIp + ":1975/v1/delegates", getHeaders()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return AJson.deserializeList(Node.class, jsonObject.get("data").toString());
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
    public Receipt transferTokens(Node contact, String privateKey, String from, String to, long tokens) throws Exception {
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
    public Receipt transferTokens(Node contact, Account fromAccount, Account toAccount, long tokens) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), toAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, tokens, System.currentTimeMillis());
            JSONObject jsonObject = new JSONObject(http.post("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }

    /*
    curl -X POST http://localhost:1175/v1/transactions -d '
    {
    		"hash":"0bb8c64779b0ab9d04b84b1d33d8cff40d4802c91cea815afcbee21b06895254",
    		"type":0,
    		"from":"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",
    		"to":"",
    		"value":0,
    		"code":"6060604052600160005534610000575b6101168061001e6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806329e99f07146046578063cb0d1c76146074575b6000565b34600057605e6004808035906020019091905050608e565b6040518082815260200191505060405180910390f35b34600057608c6004808035906020019091905050609d565b005b6000816000540290505b919050565b806000600082825401925050819055507ffa753cb3413ce224c9858a63f9d3cf8d9d02295bdb4916a594b41499014bb57f6000546040518082815260200191505060405180910390a15b505600a165627a7a723058203f0887849cabeb36c6f72cc345c5ff3521d889356357e6815dd8dbe9f7c41bbe0029",
    		"method":"",
    		"time":1526859114441,
    		"signature":"62411c9fe1b084ed6ccbe1f5a2ffc89484abce8fc7b482948d8012ab0e462b866a96d19bfd48f304c2d906c3ab950758c1d28ae7aba14e1bfb29ec8949967b4c01",
    		"hertz":0,
    		"fromName":"",
    		"toName":""
    	}'
     */
    private static final String TEST_CODE = "6060604052600160005534610000575b6101168061001e6000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806329e99f07146046578063cb0d1c76146074575b6000565b34600057605e6004808035906020019091905050608e565b6040518082815260200191505060405180910390f35b34600057608c6004808035906020019091905050609d565b005b6000816000540290505b919050565b806000600082825401925050819055507ffa753cb3413ce224c9858a63f9d3cf8d9d02295bdb4916a594b41499014bb57f6000546040518082815260200191505060405180910390a15b505600a165627a7a723058203f0887849cabeb36c6f72cc345c5ff3521d889356357e6815dd8dbe9f7c41bbe0029";
    public Receipt deployContract(Node contact, Account fromAccount) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(
            		fromAccount.getPrivateKey(), 
            		fromAccount.getAddress(), 
            		"", 
            		Transaction.Type.SMART_CONTRACT,
            		0L, 
            		System.currentTimeMillis(),
            		TEST_CODE);

            String foo = transaction.toString();
            JSONObject jsonObject = new JSONObject(http.post("http://" + contact.getEndpoint().getHost() + ":1975/v1/transactions", getHeaders(), transaction.toString()));
            receipt = (Receipt) AJson.deserialize(Receipt.class, jsonObject.toString());
            return receipt;
        }
    }
    
    /*
	curl -X POST http://localhost:1175/v1/transactions -d '
	{
		"hash":"91bbda817e38dccf939a5131d500ce110498d13c47d4dfde4ce3af558c82114d",
		"type":0,
		"from":"3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",
		"to":"c3be1a3a5c6134cca51896fadf032c4c61bc355e",
		"value":10,
		"code":"5b7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a2274657374222c0a090922636f6e7374616e74223a747275652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c2274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b7b0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a2276696577220a097d2c7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a22746573744173796e63222c0a090922636f6e7374616e74223a66616c73652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a226e6f6e70617961626c65220a097d2c7b0a09092274797065223a226576656e74222c0a0909226e616d65223a224c6f63616c4368616e6765222c0a090922616e6f6e796d6f7573223a66616c73652c0a090922696e70757473223a5b7b0a09090922696e6465786564223a66616c73652c0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d0a097d5d22",
		"method":"test",
		"time":1526859280944,
		"signature":"fc5ee5c86a7eb3fea2b7f6dc3d417ef2c87e18a5c107dfd7faf051c11dd22bf53918d8f926cdbe251624e3459e458711d1e3e76542f1d225c170279b52aee99f00",
		"hertz":0,
		"fromName":"",
		"toName":""
	}'
     */
    private final static String TEST_CODE2 = "5b7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a2274657374222c0a090922636f6e7374616e74223a747275652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c2274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b7b0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a2276696577220a097d2c7b0a09092274797065223a2266756e6374696f6e222c0a0909226e616d65223a22746573744173796e63222c0a090922636f6e7374616e74223a66616c73652c0a090922696e70757473223a5b7b0a090909226e616d65223a2269222c0a0909092274797065223a2275696e74323536220a09097d5d2c0a0909226f757470757473223a5b5d2c0a09092270617961626c65223a66616c73652c0a09092273746174654d75746162696c697479223a226e6f6e70617961626c65220a097d2c7b0a09092274797065223a226576656e74222c0a0909226e616d65223a224c6f63616c4368616e6765222c0a090922616e6f6e796d6f7573223a66616c73652c0a090922696e70757473223a5b7b0a09090922696e6465786564223a66616c73652c0a090909226e616d65223a22222c0a0909092274797065223a2275696e74323536220a09097d5d0a097d5d22";
    public Receipt executeContract(Node contact, Account fromAccount) throws Exception {
        try (Http http = new Http()) {
            Transaction transaction = Transaction.create(
            		fromAccount.getPrivateKey(), 
            		fromAccount.getAddress(), 
            		"c3be1a3a5c6134cca51896fadf032c4c61bc355e", 
            		Transaction.Type.SMART_CONTRACT,
            		10L, 
            		System.currentTimeMillis(),
            		TEST_CODE2,
            		"test",
                    "abi",
                    null);
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
    public Account getAccount(Node contact, String address) throws Exception {
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
    public List<Transaction> getTransactions(Node contact) throws Exception {
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
    public List<Transaction> getTransactionsByFromAddress(Node contact, String address) throws Exception {
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
    public List<Transaction> getTransactionsByToAddress(Node contact, String address) throws Exception {
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
    public Receipt getStatus(Node contact, String id) throws Exception {
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

            Object[] params = new Object[1];
            params[0] = "454";

            Transaction transaction = Transaction.create(
                    "0f86ea981203b26b5b8244c8f661e30e5104555068a4bd168d3e3015db9bb25a",
                    "3ed25f42484d517cdfc72cafb7ebc9e8baa52c2c",
                    "c3be1a3a5c6134cca51896fadf032c4c61bc355e",
                    Transaction.Type.SMART_CONTRACT,
                    10L,
                    System.currentTimeMillis(),
                    TEST_CODE2,
                    "abi",
                    "test",
                    params);


            String foo = transaction.toString();


            Sdk sdk = new Sdk("10.0.1.3");
            List<Node> contacts = sdk.getDelegates();
            Account genesisAccount = sdk.createAccount();
            Account toAccount = sdk.createAccount();
            Receipt receipt = sdk.transferTokens(contacts.get(0), genesisAccount, toAccount, 45);
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
