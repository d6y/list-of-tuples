# First attempt at constructing an EXP or EXP or EXP.

A nicer way:

```
val q = messages.filter( m =>
  data.collect { case (a,b) => m.x === a && m.y === b }.reduceLeft(_ || _)
)
```

via:
http://stackoverflow.com/a/26816681/154248




