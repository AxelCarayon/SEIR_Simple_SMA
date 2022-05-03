package behaviors;

import java.security.SecureRandom;
import java.util.Random;

public abstract class Randomized {

    protected Random r;

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public Randomized(long seed) {
        try{
            r = SecureRandom.getInstance("SHA1PRNG", "SUN");
        }catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        r.setSeed(seed);
    }

}
