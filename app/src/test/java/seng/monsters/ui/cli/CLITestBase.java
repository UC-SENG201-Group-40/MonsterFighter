//
//  CLITestBase.java
//  seng-monsters
//
//  Created by d-exclaimation on 14:15.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.cli;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;

public abstract class CLITestBase {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayOutputStream testOut;

    /**
     * The TestableCLI being tested
     *
     * @return The CLI itself
     */
    public abstract TestableCLI cli();

    /**
     * Base setup method to be called on <code>@BeforeEach</code>
     */
    protected void baseSetup() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    /**
     * Base teardown method to be called on <code>@AfterEach</code>
     */
    protected void baseTeardown() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    /**
     * Provide the <code>System.in</code> with a single input that can be acquired through <code>Scanner::nextLine</code>
     *
     * @param data The input as string
     */
    protected void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        cli().refreshSystemIn();
    }

    /**
     * Provide the <code>System.in</code> with multiple inputs that can be acquired through <code>Scanner::nextLine</code>
     *
     * @param data The input as a collection of string
     */
    protected void provideMultipleInput(Collection<String> data) {
        final String input = String.join("\n", data);
        provideInput(input);
    }

    /**
     * Take a snapshot of the current <code>System.out</code> and return it as String
     *
     * @return The output as string
     */
    protected String acquireOutput() {
        final String current = testOut.toString();
        baseSetup();
        return current;
    }
}
