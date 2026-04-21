
```plantuml
hide empty description

state entrypoint <<start>>
    
entrypoint --> Init : /start
Init --> Index

Index --> PoolLength : rectagle
Index --> PoolDiameter : circle

PoolLength --> PoolWidth : userInput
PoolWidth --> PoolDepth : userInput
PoolDiameter --> PoolDepth : userInput
PoolDepth --> FilterType : userInput

FilterType --> GuideType : cartridge sand no_filter

GuideType --> Guide : green_pool beginning_of_season step_by_step

Guide --> Contact : contact
Guide --> GuideType : guideType
Guide --> Index : index

Contact --> Guide : back
```

