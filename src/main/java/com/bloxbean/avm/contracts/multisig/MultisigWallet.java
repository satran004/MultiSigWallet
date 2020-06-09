package com.bloxbean.avm.contracts.multisig;

import avm.Address;
import avm.Blockchain;
import org.aion.avm.tooling.abi.Callable;
import org.aion.avm.tooling.abi.Fallback;
import org.aion.avm.tooling.abi.Initializable;
import org.aion.avm.userlib.AionList;
import org.aion.avm.userlib.AionMap;
import org.aion.avm.userlib.AionSet;
import org.aion.avm.userlib.abi.ABIDecoder;

import java.math.BigInteger;
import java.util.Map;

import static avm.Blockchain.getCaller;
import static avm.Blockchain.require;

public class MultisigWallet {

    public final static int MAX_OWNER_COUNT = 50;

    @Initializable
    public static Address[] _owners;

    @Initializable
    public static int required;

    public static AionSet<Address> owners;

    public static int transactionCount;

    //Storage
    private static Map<Long, Transaction> transactions;
    private static Map<Long, Map<Address, Boolean>> confirmations;
    private static Map<Address, Boolean> ownersMap;

    public static class Transaction {
        private Address destination;
        private BigInteger value;
        private byte[] data;
        private boolean executed;
    }

    static {
        ABIDecoder abiDecoder = new ABIDecoder(Blockchain.getData());
        _owners = abiDecoder.decodeOneAddressArray();
        required = abiDecoder.decodeOneInteger();

        validRequirement(_owners.length, required);

        owners = new AionSet<>();
        for(Address owner: _owners) {
            owners.add(owner);
        }

        ownersMap = new AionMap<>();
        transactions = new AionMap<>();
        confirmations = new AionMap<>();
    }

    @Callable
    public static void onlyWallet() {
        require(getCaller().equals(Blockchain.getAddress()));
    }

    @Callable
    public static void ownerDoesNotExist(Address owner) {
        require(ownersMap.get(owner) == null);
    }

    @Callable
    public static void ownerExists(Address owner) {
        require(ownersMap.get(owner) != null);
    }

    public static void transactionExists(long transactionId) {
        require(transactions.get(Long.valueOf(transactionId)) != null);
    }

    public static void confirmed(Long transactionId, Address owner) {
        require(confirmations.get(transactionId) != null
                && confirmations.get(transactionId).get(owner) != null);
    }

    public static void notConfirmed(Long transactionId, Address owner) {
        require(confirmations.get(transactionId) != null
                && confirmations.get(transactionId).get(owner) == null);
    }

    public static void notExecuted(Long transactionId) {
        require(transactions.get(transactionId) != null && !transactions.get(transactionId).executed);
    }

    public static void notNull(Address address) {
        require(address != null);
    }

    public static void validRequirement(int ownerCount, int _required) {
        require(ownerCount <= MAX_OWNER_COUNT
                    && _required <= ownerCount
                    && _required != 0
                    && ownerCount != 0);
    }

    @Fallback
    public static void fallback() {
        if (Blockchain.getValue() != null && Blockchain.getValue().compareTo(BigInteger.ZERO) > 0)
            Blockchain.call(Blockchain.getCaller(), Blockchain.getValue(), null, Blockchain.getRemainingEnergy());
    }

    @Callable
    public static void addOwner(Address owner) {
        onlyWallet();
        ownerDoesNotExist(owner);
        notNull(owner);
        validRequirement(owners.size() + 1, required);

        ownersMap.put(owner, true);
        owners.add(owner);

        //TODO EVENT
        //Owner Addition
    }

    @Callable
    public static void removeOwner(Address owner) {
        onlyWallet();
        ownerExists(owner);

        ownersMap.remove(owner);
        owners.remove(owner);

        if(required > owners.size())
            changeRequirement(owners.size());

        //TODO EVENT
        //Owner removal
    }

    @Callable
    public static void changeRequirement(int _required) {
        onlyWallet();

        required = _required;

        //TODO Event
        //Requirement change Event
    }



}
