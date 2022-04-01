Monsters, Items and Purchasable

```
                                                    ┌──────────────────────┐
┌──────────────────────────────────────┐            │   <<Purchasable>>    │
│              *Monster*               │            ├──────────────────────┤
├──────────────────────────────────────┤            │ + buyPrice(): int    │
│  - name: String                      │            │ + sellPrice(): int   │
│  - currentHp: int                    │            │                      │
│  - level: int                        │            └──────────────────────┘
│  - baseHp: int                       │                       ^
├──────────────────────────────────────┤                      / \
│  + *baseDamage*(): int               │                       │
│  + *speed*(): int                    │
│  + *healRate*(): double              │                       │
│  + *idealEnvironment*(): Environment ├─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┤
│  + *shouldLevelUp*(): boolean        │                       │
│  + *shouldLeave*(): boolean          │
│                                      │                       │
│  + levelUp(): void                   │
│  + healMonster(int amount): void     │                       │
│  + damageMonster(int amount): void   │
│  + maxHp(): int                      │                       │
│  + damage(env: Environment): int     │
│  + isFainted(): boolean              │                       │
│  + buyPrice(): int                   │
│  + sellPrice(): int                  │                       │
│                                      │
└──────────────────────────────────────┘                       │

                                                               │

  ┌──────────────────────────────────┐                         │
  │            *Item*                │
  ├──────────────────────────────────┤                         │
  │  - name: String                  │
  ├──────────────────────────────────┤                         │
  │  + *applyTo*(mon: Monster): void ├─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┘
  │  + *buyPrice*(): int             │
  │  + *sellPrice*(): int            │
  │                                  │
  └──────────────────────────────────┘
```
