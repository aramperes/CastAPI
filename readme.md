# Cast API

A simple API to cast and handle objects using Java 8 lambdas.

## Usage

**Cast an object**
```java
Object object = "Hello World";

Cast.cast(object, String.class, (string) -> {
    // handle cast, 'string' is java.lang.String.
    System.out.println("String: " + string.toUpperCase());
});
```

**Else-cast branch chain**
```java
Object object = "Hello World";

Cast.cast(object, Integer.class, (integer) -> {
    System.out.println("Integer: " + (integer - 1));
})
.orElseCast(Double.class, (d) -> {
    System.out.println("Double: " + (d + 1.0));
})
.orElseCast(String.class, (string) -> {
    System.out.println("String: " + string.toUpperCase());
});
```

**Null branch**
```java
Object object = null;
Cast.cast(object, String.class, (string) -> {
    System.out.println("The object is a string");
})
.orNull(() -> {
    System.out.println("The object is null");
});
```

**Throw branch**

Note that using the throw-branch breaks the chain (you cannot have other branches after it).

```java
Object object = "Hello World";

Cast.cast(object, Integer.class, (integer) -> {
    System.out.println("Object is an integer!");
    // do stuff with the integer
})
.orThrow(new IllegalArgumentException("The object is not an integer");
```

**Else branch**

Note that using the orElse branch breaks the chain (you cannot have other branches after it).

This servers as a final handler if no previous branches were successful.

```java
Object object = "Hello World";

Cast.cast(object, Integer.class, (integer) -> {
    System.out.println("Object is an integer!");
})
.orElse((o) -> {
    System.out.println("Object is not an integer.");
});
```

## License

This project is licensed under the MIT license.
