module Product exposing (..)

import Http
import Json.Decode as De
import Json.Encode as Ne


productPath : String
productPath =
    "product"


type alias Product =
    { id : String
    , name : String
    , cost : Int
    , description : String
    , path : String
    , itemCode : Int
    }



-- http and json


productDecoder : De.Decoder Product
productDecoder =
    De.map6 Product
        (De.field "id" De.string)
        (De.field "name" De.string)
        (De.field "cost" De.int)
        (De.field "description" De.string)
        (De.field "path" De.string)
        (De.field "itemCode" De.int)


productEncoder : Product -> Ne.Value
productEncoder product =
    Ne.object
        [ ( "id", Ne.string product.id )
        , ( "name", Ne.string product.name )
        , ( "cost", Ne.int product.cost )
        , ( "description", Ne.string product.description )
        , ( "path", Ne.string product.path )
        , ( "itemCode", Ne.int product.itemCode )
        ]


getAllProduct : (Result Http.Error (List Product) -> msg) -> Cmd msg
getAllProduct msg =
    Http.get
        { url = productPath
        , expect = Http.expectJson msg (De.list productDecoder)
        }


addProduct : Product -> (Result Http.Error Product -> msg) -> Cmd msg
addProduct product msg =
    Http.post
        { url = productPath
        , body = Http.jsonBody <| productEncoder product
        , expect = Http.expectJson msg productDecoder
        }


updateProduct : Product -> Product -> (Result Http.Error Product -> msg) -> Cmd msg
updateProduct old product msg =
    let
        name =
            if product.name == "" then
                old.name

            else
                product.name

        cost =
            if product.cost == 0 then
                old.cost

            else
                product.cost

        description =
            if product.description == "" then
                old.description

            else
                product.description

        path =
            if product.path == "" then
                old.path

            else
                product.path
    in
    Http.request
        { method = "PUT"
        , headers = []
        , url = productPath ++ "/" ++ product.id
        , body =
            Http.jsonBody <|
                productEncoder <|
                    Product product.id name cost description path product.itemCode
        , expect = Http.expectJson msg productDecoder
        , timeout = Nothing
        , tracker = Nothing
        }



-- validation


isValidProduct : Product -> Bool
isValidProduct product =
    let
        name =
            product.name /= ""

        cost =
            product.cost /= 0

        path =
            product.path /= ""
    in
    name && cost && path


productToString : Product -> String
productToString product =
    Ne.encode 4 <| productEncoder product
