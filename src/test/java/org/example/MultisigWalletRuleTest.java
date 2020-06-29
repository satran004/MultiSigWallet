package org.example;

import avm.Address;
import com.bloxbean.avm.contracts.multisig.MultisigWalletTestImpl;
import i.RevertException;
import org.aion.avm.core.util.Helpers;
import org.aion.avm.embed.AvmRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MultisigWalletRuleTest {
    @ClassRule
    public static AvmRule avmRule = new AvmRule(true);

    public static MultisigWalletTestImpl multisigWalletTestImpl;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void deploy() {
        multisigWalletTestImpl = new MultisigWalletTestImpl(avmRule);

        Address[] addresses = new Address[] { new Address(Helpers.hexStringToBytes("0xa02d00e708e8a865e67a06f7e7b9eeef748725ee5974d804778426de05b2ac9c")),
                new Address(Helpers.hexStringToBytes("0xa0ddeee708e8a865e67a06f7e7b9eeef748725ee5974d804778426de05b37f9c"))};
        multisigWalletTestImpl.deploy(addresses, Integer.valueOf(2));

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void addOwner_should_throw_error_when_call_directly() {
        Address owner = new Address(Helpers.hexStringToBytes("0xb11dE1fE33dCC00F3f91c7213B12e026035633b3372e5376793290bbC160a53e"));
        multisigWalletTestImpl.addOwner(owner);

        assertTrue(errContent.toString().contains("DApp execution to REVERT"));
    }

}

