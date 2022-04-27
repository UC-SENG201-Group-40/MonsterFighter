//
//  TestableCLI.java
//  seng-monsters
//
//  Created by d-exclaimation on 13:55.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.cli;

import java.util.Scanner;

/**
 * Base for a Testable CLI that takes input from the <code>System.in</code>
 */
public abstract class TestableCLI {
    /**
     * The current input
     */
    private Scanner inputScanner = new Scanner(System.in);

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
        inputScanner = new Scanner(System.in);
    }
}
