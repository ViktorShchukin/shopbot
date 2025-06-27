
```plantuml
hide empty description

state entrypoint <<start>>

state About
state Basket
state Catalog
state Index
state Init
state Order
state ProductAbout
state ForWholesaler
state PayAndDelivery
state Instruction
state DistributionMode
state OrderAdditionalInfo
    
entrypoint --> Init : /start
Init --> Index

Index --> About : about
Index --> Catalog : catalog
Index --> ForWholesaler : forWholesaler
Index --> PayAndDelivery : payAndDelivery

ForWholesaler --> Index : index
About --> Index : index
PayAndDelivery --> Index : index

Catalog --> Index : index
Catalog --> ProductAbout : productAbout?{productName}
Catalog --> Catalog : nextPage
Catalog --> Catalog : previousPage
'' get into the specific folder 
Catalog --> Catalog : folder?{folderPath}
'' go to the root of the catalog
Catalog --> Catalog : catalog
Catalog --> Basket : basket

ProductAbout --> ProductAbout : quantityMinus
ProductAbout --> ProductAbout : quantityPlus
ProductAbout --> ProductAbout : addToBasket
ProductAbout --> Basket : basket
ProductAbout --> Index : index
ProductAbout --> Catalog : catalog
ProductAbout --> Instruction : instruction

Instruction --> ProductAbout : productAbout

Basket --> Index : index
Basket --> Basket : clearBasket
Basket --> DistributionMode : doOrder

DistributionMode --> OrderAdditionalInfo : delivery
DistributionMode --> OrderAdditionalInfo : selfPickup

OrderAdditionalInfo --> Order : userInput?{input}

Order --> Index : index
```

