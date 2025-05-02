
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
state Folder
state ForWholesaler
state PayAndDelivery
state Instruction
    
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
Catalog --> Folder : folder?{folderName}
Catalog --> Catalog : nextPage
Catalog --> Catalog : previousPage

''it is not well thought out thing. Can be really changed in the future
Folder --> Catalog : catalog
Folder --> Folder : folder?{folderName}
Folder --> ProductAbout : productAbout?{productName}
Folder --> Folder : nextPage
Folder --> Folder : previousPage

ProductAbout --> ProductAbout : quantityMinus
ProductAbout --> ProductAbout : quantityPlus
ProductAbout --> ProductAbout : addToBasket
ProductAbout --> Basket : basket
ProductAbout --> Index : index
ProductAbout --> Catalog : catalog
ProductAbout --> Instruction : instruction

Instruction --> ProductAbout : productAbout

Basket --> Index : index
Basket --> Order : doOrder

Order --> Index : index
```

