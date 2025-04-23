module Main exposing (..)

import Browser
import Html exposing (Html, div, text)
import Html.Attributes
import Html.Events
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
   { products : List Product
   , productToAdd : Product
   }


-- init


init : () -> (Model, Cmd Msg)
init _ =
  let _ = Debug.log "i am working" in
  ( { products = []
    , productToAdd = Product "" "" 0 ""
    }
  , getAllProduct
  )


-- update

type Msg
    = GotProducts (Result Http.Error (List Product))
    | AddProduct
    | DeleteProduct Product
    | UpdateProduct Product
    | GotProductNameToAdd String
    | GotProductCostToAdd String
    | GotProductDescriptionToAdd String
    --| GotProduct (Result Http.Error Product)


update : Msg -> Model -> (Model, Cmd msg)
update msg model =
  case msg of
    GotProducts res -> (processGotProduct model res, Cmd.none)
    AddProduct -> (model, Cmd.none)
    DeleteProduct product -> (model, Cmd.none)
    UpdateProduct product -> (model, Cmd.none)
    GotProductNameToAdd str -> ( { model | productToAdd = updateProductName model.productToAdd str }, Cmd.none)
    GotProductCostToAdd str -> case String.toInt str of
      Just cost -> ( { model | productToAdd = updateProductCost model.productToAdd cost}, Cmd.none)
      Nothing -> (model, Cmd.none) -- todo add error handling in case where you cant cast to int.
    GotProductDescriptionToAdd str -> ( { model | productToAdd = updateProductDescription model.productToAdd str}, Cmd.none)
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

updateProductName : Product -> String -> Product
updateProductName product name =
  {product | name = name}

updateProductCost : Product -> Int -> Product
updateProductCost product cost =
  {product | cost = cost}

updateProductDescription : Product -> String -> Product
updateProductDescription product description =
  {product | description = description}

-- view


view : Model -> Html Msg
view model =
  div [] [drawProductTable model.products, drawAddProductForm]


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

drawAddProductForm : Html.Html Msg
drawAddProductForm =
  div []
    [ Html.input [ Html.Events.onInput GotProductNameToAdd, Html.Attributes.placeholder "name"] []
    , Html.input [ Html.Events.onInput GotProductCostToAdd, Html.Attributes.placeholder "cost"] []
    , Html.input [ Html.Events.onInput GotProductDescriptionToAdd, Html.Attributes.placeholder "description"] []
    , Html.button [ Html.Events.onClick AddProduct ] []
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