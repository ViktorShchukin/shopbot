
```plantuml
hide empty description

state entrypoint <<start>>
    
entrypoint --> Init : /start
Init --> Index
Index --> About : about
About --> Index : index
Index --> Products : catalog
Products --> Index : index
Products --> Product.About : product/${name}/about
```

