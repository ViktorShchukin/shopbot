module Main exposing (..)

import Browser
import Html exposing (Html, div, text)
import Html.Attributes
import Html.Events
import Http exposing (Error(..))
import Product exposing (..)

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

type alias LogEntry =
  { message : String
  --, timestamp : Time.Posix
  }

type alias Model =
   { products : List Product
   , productToAdd : Product
   , productToUpdate : List Product
   , logs : List LogEntry
   }


-- init


init : () -> (Model, Cmd Msg)
init _ =
  ( { products = []
    , productToAdd = Product "" "" 0 "" ""
    , productToUpdate = []
    , logs = []
    }
  , getAllProduct GotProducts
  )


-- update

type Msg
    = GotProducts (Result Http.Error (List Product))
    | GotProduct (Result Http.Error Product)
    | AddProduct
    | UpdateProduct Product
    | DeleteProduct Product

    | GotProductNameToAdd String
    | GotProductCostToAdd String
    | GotProductDescriptionToAdd String
    | GotProductPathToAdd String

    | GotProductNameToUpdate Product String
    | GotProductCostToUpdate Product String
    | GotProductDescriptionToUpdate Product String
    | GotProductPathToUpdate Product String

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    GotProducts res -> (processGotProducts model res, Cmd.none)
    -- todo make processGotProduct which should filter model.products, find by id, if exists update else add
    GotProduct res -> (model, getAllProduct GotProducts)
    AddProduct -> (model, addProduct model.productToAdd GotProduct)
    UpdateProduct product -> case findProductById model.productToUpdate product.id of
                                Nothing -> (model, Cmd.none)
                                Just prod ->  (model, updateProduct product prod GotProduct)
    DeleteProduct product -> (model, Cmd.none)

    GotProductNameToAdd str -> ( { model | productToAdd = updateProductName model.productToAdd str }, Cmd.none)
    GotProductCostToAdd str -> case String.toInt str of
      Just cost -> ( { model | productToAdd = updateProductCost model.productToAdd cost}, Cmd.none)
      Nothing -> (model, Cmd.none) -- todo add error handling in case where you cant cast to int.
    GotProductDescriptionToAdd str -> ( { model | productToAdd = updateProductDescription model.productToAdd str}, Cmd.none)
    GotProductPathToAdd pth ->  ( { model | productToAdd =  updateProductPath model.productToAdd pth}, Cmd.none)

    GotProductNameToUpdate prod str -> (gotProductNameToUpdate model prod str, Cmd.none)
    GotProductCostToUpdate prod str -> case String.toInt str of
      Just cost -> (gotProductCostToUpdate model prod cost, Cmd.none)
      -- todo add error handling in case where you cant cast to int.
      Nothing -> (model, Cmd.none)
    GotProductDescriptionToUpdate prod str -> (gotProductDescriptionToUpdate model prod str, Cmd.none)
    GotProductPathToUpdate prod str -> (gotProductPathToUpdate model prod str, Cmd.none)


processGotProducts : Model -> (Result Http.Error (List Product)) -> Model
processGotProducts model res =
  case res of
    Ok prod -> { model | products = prod }
    Err err -> logHttpErr model err

myTag = "Main.elm"

logHttpErr : Model -> Http.Error -> Model
logHttpErr model err =
  { model | logs = List.append model.logs [httpErrorToLogEntry err] }

httpErrorToLogEntry : Http.Error -> LogEntry
httpErrorToLogEntry err =
  LogEntry (httpErrorToString err) --Time.now

httpErrorToString : Http.Error -> String
httpErrorToString err =
    case err of
      BadUrl str -> "ERROR " ++ myTag ++ " HTTP BadUrl: " ++ str
      Timeout -> "ERROR " ++ myTag ++ " HTTP Timeout"
      NetworkError -> "ERROR " ++ myTag ++ " HTTP NetworkErr"
      BadStatus int -> "ERROR " ++ myTag ++ " HTTP BadStatus: " ++ (String.fromInt int)
      BadBody str -> "ERROR " ++ myTag ++ " HTTP BadBody: " ++ str


gotProductNameToUpdate : Model -> Product -> String -> Model
gotProductNameToUpdate model prod str =
  let productList = case (findProductById model.productToUpdate prod.id) of
                      Just product ->
                        let
                            updated = updateProductName product str
                        in
                          List.map (\prod1 -> if prod1.id == updated.id then updated else prod1) model.productToUpdate
                      Nothing -> List.append model.productToUpdate [updateProductName prod str]
  in
    { model | productToUpdate = productList}

gotProductCostToUpdate : Model -> Product -> Int -> Model
gotProductCostToUpdate model prod cost =
  let productList = case (findProductById model.productToUpdate prod.id) of
                      Just product ->
                        let
                            updated = updateProductCost product cost
                        in
                          List.map (\prod1 -> if prod1.id == updated.id then updated else prod1) model.productToUpdate
                      Nothing -> List.append model.productToUpdate [updateProductCost prod cost]
  in
    { model | productToUpdate = productList}

gotProductDescriptionToUpdate : Model -> Product -> String -> Model
gotProductDescriptionToUpdate model prod str =
  let productList = case (findProductById model.productToUpdate prod.id) of
                      Just product ->
                        let
                            updated = updateProductDescription product str
                        in
                          List.map (\prod1 -> if prod1.id == updated.id then updated else prod1) model.productToUpdate
                      Nothing -> List.append model.productToUpdate [updateProductDescription prod str]
  in
    { model | productToUpdate = productList}

gotProductPathToUpdate : Model -> Product -> String -> Model
gotProductPathToUpdate model prod str =
  let productList = case (findProductById model.productToUpdate prod.id) of
                      Just product ->
                        let
                            updated = updateProductPath product str
                        in
                          List.map (\prod1 -> if prod1.id == updated.id then updated else prod1) model.productToUpdate
                      Nothing -> List.append model.productToUpdate [updateProductPath prod str]
  in
    { model | productToUpdate = productList}

findProductById : List Product -> String -> Maybe Product
findProductById productList id =
  List.filter (\prod -> prod.id == id) productList |> List.head

updateProductId : Product -> String -> Product
updateProductId product id =
  {product | id = id}

updateProductName : Product -> String -> Product
updateProductName product name =
  {product | name = name}

updateProductCost : Product -> Int -> Product
updateProductCost product cost =
  {product | cost = cost}

updateProductDescription : Product -> String -> Product
updateProductDescription product description =
  {product | description = description}

updateProductPath : Product -> String -> Product
updateProductPath product pth =
  { product | path = pth}

-- view


view : Model -> Html Msg
view model =
  div [] [ drawAddProductForm
         , drawProductTable model.products
         , text "--- ниже будут печататься ошибки. Если они возникнут, то прошу сообщить мне ---"
         , drawLogs model.logs
         ]


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
    , Html.th [] [text "product path"]
    ]


drawProductRow : Product -> Html.Html Msg
drawProductRow product =
  Html.tr []
    [ Html.td [] [ text product.name
                 , Html.input [ Html.Events.onInput <| GotProductNameToUpdate product, Html.Attributes.placeholder "name"] []
                 ]
    , Html.td [] [ text <| String.fromInt product.cost
                 , Html.input [ Html.Events.onInput <| GotProductCostToUpdate product, Html.Attributes.placeholder "cost"] []
                 ]
    , Html.td [] [ text product.description
                 , Html.input [ Html.Events.onInput <| GotProductDescriptionToUpdate product, Html.Attributes.placeholder "description"] []
                 ]
    , Html.td [] [ text product.path
                 , Html.input [ Html.Events.onInput <| GotProductPathToUpdate product, Html.Attributes.placeholder "path"] []
                 ]
    , Html.td [] [ Html.button [ Html.Events.onClick <| UpdateProduct product] [ text "update"]]
    ]


drawAddProductForm : Html.Html Msg
drawAddProductForm =
  Html.fieldset [ role "group"]
    [ Html.input [ Html.Events.onInput GotProductNameToAdd, Html.Attributes.placeholder "name"] []
    , Html.input [ Html.Events.onInput GotProductCostToAdd, Html.Attributes.placeholder "cost"] []
    , Html.input [ Html.Events.onInput GotProductDescriptionToAdd, Html.Attributes.placeholder "description"] []
    , Html.input [ Html.Events.onInput GotProductPathToAdd, Html.Attributes.placeholder "path"] []
    , Html.button [ Html.Events.onClick AddProduct ] [ text "add product"]
    ]

drawLogs : List LogEntry -> Html.Html Msg
drawLogs logs =
  div [] <| List.map drawLogLine logs

drawLogLine : LogEntry -> Html.Html Msg
drawLogLine log =
  Html.p [] [text log.message]

-- subscriptions

subscriptions : Model -> Sub Msg
subscriptions _ = Sub.none

-- http

role: String -> Html.Attribute msg
role value =
    Html.Attributes.attribute "role" value