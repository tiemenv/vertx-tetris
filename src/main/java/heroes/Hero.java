package heroes;

import game.Bucket;

public interface Hero {
    void useFirstAbility();

    Bucket useFirstAbility(Bucket bucket);

    void useSecondAbility();

    Bucket useSecondAbility(Bucket bucket);

    String getName();

    Bucket getBucket();

    boolean offensiveAbilityOne();
    boolean isCooldown1();

    boolean offensiveAbilityTwo();
    boolean isCooldown2();

    void setBucket(Bucket bucket);
    void setOnCooldown1(Boolean cooldown1);
    void setOnCooldown2(Boolean cooldown2);
}
