//
//  State.java
//  seng-monsters
//
//  Created by d-exclaimation on 21:04.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.state;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * State class similar to <code>Observable</code> pattern but much simpler.
 * <ul>
 * <li>Will perform callbacks on when <code>set</code> is called</li>
 * <li>Will <b>not</b> perform callback when value is mutated but not reassigned (i.e. a class has its nested properties changed)</li>
 * <li>Will <b>not</b> perform callback when value is reassigned to the same value</li>
 * <li>Suitable for storing primitives and immutable values</li>
 * </ul>
 *
 * @param <T> The type value of the state
 */
public final class State<T> {
    /**
     * The inner value
     */
    private T innerState;
    /**
     * The only listener / callback when a change happened
     */
    private Consumer<T> listener;

    private State(T state) {
        innerState = state;
    }

    /**
     * Get the current value
     *
     * @return The value as of now
     */
    public T get() {
        return innerState;
    }

    /**
     * Set the state with a brand new one.
     * <p>
     * Will <b>not</b> perform anything if the new value is identical to the current one (based of <code>Objects.equals</code>
     *
     * @param newState The new value
     */
    public void set(T newState) {
        if (Objects.equals(innerState, newState))
            return;
        innerState = newState;
        listener.accept(innerState);
    }

    /**
     * Set the state by applying a transformation to the current state.
     *
     * <b>Careful</b> with mutating the previous value, as it does not guarantee callback will be called
     *
     * @param function The operation to be performed
     */
    public void set(UnaryOperator<T> function) {
        final T res = function.apply(innerState);
        set(res);
    }

    /**
     * Apply a listener to always be called when the state has changed
     *
     * @param consumer The consumer function to reflect the new change
     */
    public void onChange(Consumer<T> consumer) {
        listener = consumer;
    }

    /**
     * Create a new instance of the State
     *
     * @param initialState The initial value
     * @param <T>          The type of the value
     * @return A State that behaves like a simplified <code>Observable</code>
     */
    public static <T> State<T> of(T initialState) {
        return new State<>(initialState);
    }
}
