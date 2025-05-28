module Product exposing (..)

import Http
import Json.Decode as De
import Json.Encode as Ne

productPath : String
productPath = "product"

type alias Product =
  { id : String
  , name : String
  , cost : Int
  , description : String
  , path : String
  }


productDecoder : De.Decoder Product
productDecoder =
  De.map5 Product
    (De.field "id" De.string)
    (De.field "name" De.string)
    (De.field "cost" De.int)
    (De.field "description" De.string)
    (De.field "path" De.string)

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
    , body = Http.jsonBody <| Ne.object [ ("name", Ne.string product.name)
                                     , ("cost", Ne.int product.cost)
                                     , ("description", Ne.string product.description)
                                     , ("path", Ne.string product.path)
                                     ]
    , expect = Http.expectJson msg productDecoder
    }

updateProduct : Product -> Product -> (Result Http.Error Product -> msg) -> Cmd msg
updateProduct old product msg =
  let
      name = if product.name == "" then old.name else product.name
      cost = if product.cost == 0 then old.cost else product.cost
      description = if product.description == "" then old.description else product.description
      path = if product.path == "" then old.path else product.path
  in

  Http.request
    { method = "PUT"
    , headers = []
    , url = productPath ++ "/" ++ product.id
    , body = Http.jsonBody <| Ne.object [ ("name", Ne.string name)
                                     , ("cost", Ne.int cost)
                                     , ("description", Ne.string description)
                                     , ("path", Ne.string path)
                                     ]
    , expect = Http.expectJson msg productDecoder
    , timeout = Nothing
    , tracker = Nothing
    }