
```plantuml
state elsewhere <<start>>

[*] --> Init : /start
elsewhere --> Start : index
Init --> Start
Start --> About : about
Start --> Products : catalog
Products --> Product.About : product/${name}/about

state UnknownCommand
```

