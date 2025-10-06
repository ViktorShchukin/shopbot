
```plantuml
hide empty description

state entrypoint <<start>>

state About
state Contact
state Shop
state Guide
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


Index --> Contact : contact
Index --> Shop : shop

Index --> PoolType : poolType
PoolType --> PoolSizeInfo : rectangle
PoolType --> PoolSizeInfo : circle
PoolSizeInfo --> Guide : userInput?{input}
Guide --> Index

Contact --> About : about
Contact --> ForWholesaler : forWholesaler
Contact --> Index : index

ForWholesaler --> Contact : contact
About --> Contact : contact

Shop --> Catalog : catalog
Shop --> PayAndDelivery : payAndDelivery
Shop --> Basket : basket
Shop --> Index : index

PayAndDelivery --> Shop : shop

Catalog --> Shop : shop
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
'' ProductAbout --> Shop : shop
ProductAbout --> Catalog : catalog
ProductAbout --> Instruction : instruction

Instruction --> ProductAbout : productAbout

Basket --> Catalog : catalog
Basket --> Basket : clearBasket
Basket --> DistributionMode : doOrder

DistributionMode --> OrderAdditionalInfo : delivery
DistributionMode --> OrderAdditionalInfo : selfPickup

OrderAdditionalInfo --> Order : userInput?{input}

Order --> Index : index
```

