package ca.momoperes.casts;

public final class Cast<O> {
    private static final String TARGET_NOT_NULL = "Target class cannot be null.";
    private static final String CALLBACK_NOT_NULL = "Cast callback cannot be null.";
    private static final String THROWABLE_NOT_NULL = "Throwable cannot be null.";

    private final O object;
    private boolean complete;

    private Cast(O object) {
        this.object = object;
    }

    /**
     * Attempts to cast the object to the given target class.
     *
     * @param target   the target class
     * @param callback the callback to handle this cast, if successful
     * @param <T>      the type of the target class
     * @return the {@link Cast} object to continue the chain
     * @throws IllegalArgumentException if the target or the callback are null
     */
    public <T> Cast<O> cast(Class<T> target, CastHandler<T> callback) {
        if (target == null) {
            throw new IllegalArgumentException(TARGET_NOT_NULL);
        }
        if (callback == null) {
            throw new IllegalArgumentException(CALLBACK_NOT_NULL);
        }
        if (target.isInstance(object)) {
            complete = true;
            callback.cast((T) object);
        }
        return this;
    }

    /**
     * Attempts to cast the object to the given target class, if the cast was not previously completed.
     *
     * @param target   the target class
     * @param callback the callback to handle this cast, if successful
     * @param <T>      the type of the target class
     * @return the {@link Cast} object to continue the chain
     * @throws IllegalArgumentException if the target or the callback are null
     */
    public <T> Cast<O> orElseCast(Class<T> target, CastHandler<T> callback) {
        if (target == null) {
            throw new IllegalArgumentException(TARGET_NOT_NULL);
        }
        if (callback == null) {
            throw new IllegalArgumentException(CALLBACK_NOT_NULL);
        }
        if (complete) {
            return this;
        }
        return cast(target, callback);
    }

    /**
     * Executes the given callback with the base object if the cast was not previously completed.
     *
     * @param callback the callback to handle the base object
     * @throws IllegalArgumentException if the callback is null
     */
    public void orElse(CastHandler<O> callback) {
        if (callback == null) {
            throw new IllegalArgumentException(CALLBACK_NOT_NULL);
        }
        if (complete) {
            return;
        }
        callback.cast(object);
    }

    /**
     * Throws the given {@link Throwable} if the cast was not previously completed.
     *
     * @param throwable the {@link Throwable} to throw
     * @param <T>       the type of the {@link Throwable}
     * @throws T                        the {@link Throwable}
     * @throws IllegalArgumentException if the throwable is null
     */
    public <T extends Throwable> void orThrow(T throwable) throws T {
        if (throwable == null) {
            throw new IllegalArgumentException(THROWABLE_NOT_NULL);
        }
        if (complete) {
            return;
        }
        throw throwable;
    }

    /**
     * Executes the given {@link NullHandler} if the object is null, and the cast was not previously completed.
     *
     * @param callback the {@link NullHandler} to handle this case
     * @return the {@link Cast} object to continue the chain
     * @throws IllegalArgumentException if the callback is null
     */
    public Cast<O> orNull(NullHandler callback) {
        if (callback == null) {
            throw new IllegalArgumentException(CALLBACK_NOT_NULL);
        }
        if (complete) {
            return this;
        }
        if (object == null) {
            callback.handle();
            complete = true;
        }
        return this;
    }

    /**
     * Initializes a {@link Cast} chain and attempts to cast the object to the given target class.
     *
     * @param object   the object to cast
     * @param target   the target class
     * @param callback the callback to handle the cast, if successful
     * @param <T>      the type of the target class
     * @param <O>      the type of the object
     * @return the {@link Cast} object to continue the chain
     * @throws IllegalArgumentException if the target or the callback are null
     */
    public static <T, O> Cast<O> cast(O object, Class<T> target, CastHandler<T> callback) {
        if (target == null) {
            throw new IllegalArgumentException(TARGET_NOT_NULL);
        }
        if (callback == null) {
            throw new IllegalArgumentException(CALLBACK_NOT_NULL);
        }
        return new Cast<>(object).cast(target, callback);
    }

    /**
     * Represents a handler for a cast to a target type
     *
     * @param <T> the target type
     */
    @FunctionalInterface
    public interface CastHandler<T> {
        /**
         * Called when the object was successfully casted.
         *
         * @param casted the casted object
         */
        void cast(T casted);
    }

    /**
     * Represents a handler for a null object
     */
    @FunctionalInterface
    public interface NullHandler {
        /**
         * Called when the object is null.
         */
        void handle();
    }
}
