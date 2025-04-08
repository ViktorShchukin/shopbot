
```plantuml
hide empty description

state anyUpdate <<start>>

state Resolve
state UnknownCommand

state UserSession {
    state startChatSession <<entrypoint>>
    state history <<history>>
    
    startChatSession --> Init : /start
    Init --> Start
    Start --> About : about
    Start --> Products : catalog
    Products --> Product.About : product/${name}/about
}

anyUpdate --> Resolve
note on link: on update received 

Resolve --> startChatSession
Resolve --> UnknownCommand
Resolve --> history
note on link: if session already\nexists restore it


```

