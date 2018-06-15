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
            Transaction transaction = Transaction.create(privateKey, from, to, Transaction.Type.TRANSFER_TOKENS, tokens, System.currentTimeMillis(), true);
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
            Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), toAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, tokens, System.currentTimeMillis(), true);
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
            		TEST_CODE,
                    true);

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
                    null,
                    true);
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
        Transaction transaction = Transaction.create(fromAccount.getPrivateKey(), fromAccount.getAddress(), genesisAccount.getAddress(), Transaction.Type.TRANSFER_TOKENS, tokens, 0, true);
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
                    "",
                    Transaction.Type.SMART_CONTRACT,
                    0,
                    System.currentTimeMillis(),
                    "608060405234801561001057600080fd5b506040805190810160405280600d81526020017f61616161616161616161616161000000000000000000000000000000000000008152506000908051906020019061005c9291906100f7565b50600060016000018190555060006001800160006101000a81548160ff02191690831515021790555060018060010160016101000a81548160ff021916908360ff1602179055506040805190810160405280600b81526020017f6262626262626262626262000000000000000000000000000000000000000000815250600160020190805190602001906100f19291906100f7565b5061019c565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061013857805160ff1916838001178555610166565b82800160010185558215610166579182015b8281111561016557825182559160200191906001019061014a565b5b5090506101739190610177565b5090565b61019991905b8082111561019557600081600090555060010161017d565b5090565b90565b610664806101ab6000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806333e538e91461007d57806334e45f531461010d5780633a458b1f1461017657806378d8866e1461022557806379af6473146102b5578063cb69e300146102cc575b600080fd5b34801561008957600080fd5b50610092610335565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100d25780820151818401526020810190506100b7565b50505050905090810190601f1680156100ff5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561011957600080fd5b50610174600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506103d7565b005b34801561018257600080fd5b5061018b6103f4565b60405180858152602001841515151581526020018360ff1660ff16815260200180602001828103825283818151815260200191508051906020019080838360005b838110156101e75780820151818401526020810190506101cc565b50505050905090810190601f1680156102145780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561023157600080fd5b5061023a6104c4565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561027a57808201518184015260208101905061025f565b50505050905090810190601f1680156102a75780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156102c157600080fd5b506102ca610562565b005b3480156102d857600080fd5b50610333600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610579565b005b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103cd5780601f106103a2576101008083540402835291602001916103cd565b820191906000526020600020905b8154815290600101906020018083116103b057829003601f168201915b5050505050905090565b80600160020190805190602001906103f0929190610593565b5050565b60018060000154908060010160009054906101000a900460ff16908060010160019054906101000a900460ff1690806002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ba5780601f1061048f576101008083540402835291602001916104ba565b820191906000526020600020905b81548152906001019060200180831161049d57829003601f168201915b5050505050905084565b60008054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561055a5780601f1061052f5761010080835404028352916020019161055a565b820191906000526020600020905b81548152906001019060200180831161053d57829003601f168201915b505050505081565b600160000160008154809291906001019190505550565b806000908051906020019061058f929190610593565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106105d457805160ff1916838001178555610602565b82800160010185558215610602579182015b828111156106015782518255916020019190600101906105e6565b5b50905061060f9190610613565b5090565b61063591905b80821115610631576000816000905550600101610619565b5090565b905600a165627a7a7230582026a289af0b033267e3fc13869c446b0552e2c62b9b3fa46bc626be2c683528680029",
                    true);


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
