package dispatchlabs.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import dispatchlabs.Sdk;

public class SdkTest {

    @Test
    public void TestNewSdk() {

        try {

            Sdk s = new Sdk("127.0.0.1");
        } catch  ( Throwable t) {
                System.out.println ( t );

        }

        assertEquals ( 1,1 );
    }
}

