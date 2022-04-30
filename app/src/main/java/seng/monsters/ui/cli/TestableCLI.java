//
//  TestableCLI.java
//  seng-monsters
//
//  Created by d-exclaimation on 13:55.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.cli;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Base for a Testable CLI that takes input from the <code>System.in</code>
 */
public abstract class TestableCLI {
    /**
     * The current input
     */
    private Scanner inputScanner = TestableCLI.customInputScanner();

    /**
     * Get the input as of now
     *
     * @return The input as Scanner
     */
    public final Scanner input() {
        return inputScanner;
    }

    /**
     * Refresh the input scanner to have the updated <code>System.in</code>
     */
    protected final void refreshSystemIn() {
        inputScanner = TestableCLI.customInputScanner();
    }

    private static Scanner customInputScanner() {
        final var inputScanner = new Scanner(System.in);
        inputScanner.useDelimiter("\\n|\\r\\n|\\r");
        return inputScanner;
    }
}
