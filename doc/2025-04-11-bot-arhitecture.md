```plantuml
'scale 00 width
'scale 900 height

state Bot {
    state in <<entrypoint>>
    state out <<exitpoint>>

    state "Bot(Update consumer)" as b
    state FsmRunner
    state View
    
    in --> b : Update
    b --> FsmRunner : Command
    FsmRunner --> b : Result
    b --> View : Result
    View --> out : TelegramMessage
}

'state Service
state UpdateMiner
state ResultDistributor
state ForeignApp

ForeignApp --> UpdateMiner : Update Http Request
UpdateMiner --> in
out --> ResultDistributor
ResultDistributor -u-> ForeignApp : Message\nHttp response

'Service --> FsmRunner : data
'FsmRunner --> Service : query

UpdateMiner: can be Webhook based\nor longPooling
ResultDistributor: In most cases just a client\nthat sends an response\nin appropriate format
```