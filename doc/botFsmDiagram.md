
```plantuml
hide empty description

state entrypoint <<start>>

state Init
state Index
state About
state Catalog
state Folder
state ProductAbout
state Basket
state ForWholesaler
    
entrypoint --> Init : /start
Init --> Index

Index --> About : about
Index --> Catalog : catalog
Index --> ForWholesaler : forWholesaler

ForWholesaler --> Index : index

About --> Index : index

Catalog --> Index : index
Catalog --> ProductAbout : productAbout?{productName)
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

'' we haven't discussed ordering yet, so i think that it is 
'' silly idea to do predictions about it. It may look really 
'' different at the mom's point. I prefer to postpone it 
'Basket --> Order
```

