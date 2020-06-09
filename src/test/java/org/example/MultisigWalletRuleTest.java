package org.example;

import avm.Address;
import com.bloxbean.avm.contracts.multisig.MultisigWalletTestImpl;
import org.aion.avm.core.util.Helpers;
import org.aion.avm.embed.AvmRule;
import org.junit.Before;
import org.junit.ClassRule;

public class MultisigWalletRuleTest {
    @ClassRule
    public static AvmRule avmRule = new AvmRule(true);

//    //default address with balance
//    private static Address from = avmRule.getPreminedAccount();
//
//    private static Address dappAddr;
//
//    @BeforeClass
//    public static void deployDapp() {
//        //deploy Dapp:
//        // 1- get the Dapp byes to be used for the deploy transaction
//        // 2- deploy the Dapp and get the address.
//        byte[] dapp = avmRule.getDappBytes(com.bloxbean.avm.contracts.multisig.MultisigWallet.class, null);
//        dappAddr = avmRule.deploy(from, BigInteger.ZERO, dapp).getDappAddress();
//    }

    public static MultisigWalletTestImpl multisigWalletTestImpl;

    @Before
    public void deploy() {
        multisigWalletTestImpl = new MultisigWalletTestImpl(avmRule);

        Address[] addresses = new Address[] { new Address(Helpers.hexStringToBytes("0xa02d00e708e8a865e67a06f7e7b9eeef748725ee5974d804778426de05b2ac9c")),
                new Address(Helpers.hexStringToBytes("0xa0ddeee708e8a865e67a06f7e7b9eeef748725ee5974d804778426de05b37f9c"))};
        multisigWalletTestImpl.deploy(addresses, Integer.valueOf(2));
    }

//    @Test
//    public void getString() {
//        Address owner = new Address(Helpers.hexStringToBytes("0xb11dE1fE33dCC00F3f91c7213B12e026035633b3372e5376793290bbC160a53e"));
//        multisigWalletTestImpl.addOwner(owner);
//
//       // multisigWalletTestImpl.ownerDoesNotExist(owner);
//    }

}

