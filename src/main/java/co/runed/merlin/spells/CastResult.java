package co.runed.merlin.spells;

public class CastResult {
    private final Result result;
    private String message = null;

    public CastResult(Result result, String message) {
        this(result);

        this.message = message;
    }

    public CastResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return this.result == Result.SUCCESS;
    }

    public boolean shouldContinue() {
        return this.result == Result.SUCCESS || this.result == Result.SKIP;
    }

    /* Success! Continue and consume costs */
    public static CastResult success() {
        return CastResult.success(null);
    }

    public static CastResult success(String message) {
        return new CastResult(Result.SUCCESS, message);
    }

    /* Continue without consuming costs/cooldown */
    public static CastResult skip() {
        return CastResult.skip(null);
    }

    public static CastResult skip(String message) {
        return new CastResult(Result.SKIP, message);
    }

    /* Failed casting, show error and don't consume costs/cooldown */
    public static CastResult fail() {
        return CastResult.fail(null);
    }

    public static CastResult fail(String message) {
        return new CastResult(Result.FAIL, message);
    }

    public enum Result {
        SUCCESS,
        FAIL,
        SKIP
    }
}
