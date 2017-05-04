# Attempt at constructing an EXP or EXP or EXP.

```
val q = messages.filter( m =>
  data.collect { case (a,b) => m.x === a && m.y === b }.reduceLeft(_ || _)
)
```

from:
http://stackoverflow.com/a/26816681/154248




