package heroes;

import game.Bucket;
import org.pmw.tinylog.Logger;

public class Jesse implements Hero {
    private static final String NAME = "Jesse";
    private Bucket bucket;
    private boolean isFirstAbilityOffensive;
    private boolean isSecondAbilityOffensive = true;
    private boolean isOnCooldown1;
    private boolean isOnCooldown2;

    public Jesse() {
        this.bucket = new Bucket(10, 24);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Bucket getBucket() {
        return bucket;
    }

    @Override
    public boolean offensiveAbilityOne() {
        return isFirstAbilityOffensive;
    }

    @Override
    public boolean isCooldown1() {
        return isOnCooldown1;
    }

    @Override
    public boolean offensiveAbilityTwo() {
        return isSecondAbilityOffensive;
    }

    @Override
    public boolean isCooldown2() {
        return isOnCooldown2;
    }


    @Override
    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public void setOnCooldown1(Boolean cooldown1) {
        isOnCooldown1 = cooldown1;
    }

    @Override
    public void setOnCooldown2(Boolean cooldown2) {
        isOnCooldown2 = cooldown2;
    }

    @Override
    public void useFirstAbility() {
        bucket.removeLastRow();
        Logger.info("using first Jesse ability");
    }

    @Override
    public Bucket useFirstAbility(Bucket bucket) {
        return null;
    }

    @Override
    public void useSecondAbility() {
        bucket.removeRandomPieces();
        Logger.info("Nothing to do.");
    }

    @Override
    public Bucket useSecondAbility(Bucket enemyBucket) {
        enemyBucket.removeColumn();
        return enemyBucket;
    }



}
