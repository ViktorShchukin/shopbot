module Main exposing (..)

import Browser
import Html exposing (Html, div, text)
import Http exposing (Error(..))
import Json.Decode exposing (Decoder, field, map4)

-- main


main : Program () Model Msg
main =
  Browser.element
    { init = init
    , view = view
    ,update = update
    , subscriptions = subscriptions
    }


-- model


type alias Product =
  { id : String
  , name : String
  , cost : Int
  , description : String
  }


type alias Model =
   { products : List Product }


-- init


init : () -> (Model, Cmd Msg)
init _ =
  let _ = Debug.log "i am working" in
  ( { products = [] }
  , getAllProduct
  )


-- update

type Msg
    = GotProducts (Result Http.Error (List Product))
    | AddProduct Product
    | DeleteProduct Product
    | UpdateProduct Product
    --| GotProduct (Result Http.Error Product)


update : Msg -> Model -> (Model, Cmd msg)
update msg model =
  case msg of
    GotProducts res -> (processGotProduct model res, Cmd.none)
    AddProduct product -> (model, Cmd.none)
    DeleteProduct product -> (model, Cmd.none)
    UpdateProduct product -> (model, Cmd.none)
    --GotProduct res -> (model, Cmd.none)


processGotProduct : Model -> (Result Http.Error (List Product)) -> Model
processGotProduct model res =
  case res of
    Ok prod -> { model | products = prod }
    Err err -> let _ = (logHttpErr err) in model

myTag = "Main.elm"

logHttpErr : Http.Error -> Http.Error
logHttpErr err =
  case err of
    BadUrl str -> let _ = ( "ERROR " ++ myTag ++ " HTTP BadUrl: " ++ str |> Debug.log ) in BadUrl str
    Timeout -> let _ = ( "ERROR " ++ myTag ++ " HTTP Timeout" |> Debug.log ) in Timeout
    NetworkError -> let _ = ( "ERROR " ++ myTag ++ " HTTP NetworkErr" |> Debug.log ) in NetworkError
    BadStatus int -> let _ = ( "ERROR " ++ myTag ++ " HTTP BadStatus: " ++ (String.fromInt int) |> Debug.log ) in BadStatus int
    BadBody str -> let _ = ( "ERROR " ++ myTag ++ " HTTP BadBody: " ++ str |> Debug.log ) in BadBody str



-- view


view : Model -> Html Msg
view model =
  div [] [drawProductTable model.products]


drawProductTable: List Product -> Html.Html Msg
drawProductTable productList =
  Html.table []
    [ Html.thead [] [drawProductTableHeader]
    , Html.tbody [] (List.map drawProductRow productList)
    ]


drawProductTableHeader : Html.Html Msg
drawProductTableHeader =
  Html.tr []
    [ Html.th [] [text "product name"]
    , Html.th [] [text "product cost"]
    , Html.th [] [text "product description"]
    ]


drawProductRow : Product -> Html.Html Msg
drawProductRow product =
  Html.tr []
    [ Html.td [] [text product.name]
    , Html.td [] [text <| String.fromInt product.cost]
    , Html.td [] [text product.description]
    ]

-- subscriptions

subscriptions : Model -> Sub Msg
subscriptions _ = Sub.none

-- http

getAllProduct: Cmd Msg
getAllProduct =
  Http.get
    { url = "/product"
    , expect = Http.expectJson GotProducts (Json.Decode.list productDecoder)
    }

productDecoder : Decoder Product
productDecoder =
  map4 Product
    (field "id" Json.Decode.string)
    (field "name" Json.Decode.string)
    (field "cost" Json.Decode.int)
    (field "description" Json.Decode.string)