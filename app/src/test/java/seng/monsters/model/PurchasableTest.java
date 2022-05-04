package seng.monsters.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PurchasableTest {
    private Purchasable purchasable;

    @BeforeEach
    void setUp() {
        purchasable = new Purchasable() {
            @Override
            public int buyPrice() {
                return 100;
            }

            @Override
            public int sellPrice() {
                return buyPrice() / 2;
            }

            @Override
            public String getName() { return "name"; }
        };
    }

    @Test
    void buyPrice() {
        assertEquals(100,  purchasable.buyPrice());
    }

    @Test
    void sellPrice() {
        assertEquals(purchasable.buyPrice() / 2, purchasable.sellPrice());
    }

    @Test
    void getName() { assertEquals(Objects.hash("name"), Objects.hash(purchasable.getName())); }
}