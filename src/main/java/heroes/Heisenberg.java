package heroes;

import game.Bucket;
import io.vertx.core.Vertx;
import org.pmw.tinylog.Logger;

public class Heisenberg implements Hero {
    private static final String NAME = "Heisenberg";
    private Bucket bucket;
    private boolean isFirstAbilityOffensive = true;
    private boolean isSecondAbilityOffensive = true;
    private boolean isOnCooldown1;
    private boolean isOnCooldown2;
    private Vertx vertx = Vertx.vertx();
    private long secondAbilityTimer;

    public Heisenberg() {
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
        Logger.info("using first Heisenberg ability");
    }

    @Override
    public Bucket useFirstAbility(Bucket enemyBucket) {
        enemyBucket.removeRandomPieces();
        return enemyBucket;
    }

    @Override
    public void useSecondAbility() {
        bucket.removeRandomPieces();
        Logger.info("Nothing to do");
    }

    @Override
    public Bucket useSecondAbility(Bucket enemyBucket) {
        Logger.info("using second Heisenberg ability");
        secondAbilityTimer = vertx.setPeriodic(200, id -> {
            enemyBucket.rotate();
        });

        vertx.setTimer(5000, id -> {
            vertx.cancelTimer(secondAbilityTimer);
        });

        return enemyBucket;
    }
}
