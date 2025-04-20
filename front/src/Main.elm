module Main exposing (..)

import Browser
import Html exposing (Html, div, text)
import Http
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
  ( { products = [] }
  , getAllProduct
  )

-- update

type Msg
    = GotProduct (Result Http.Error (List Product))

update : Msg -> Model -> (Model, Cmd msg)
update msg model =
  case msg of
    GotProduct res -> (model, Cmd.none)

processGotProduct : Model -> (Result Http.Error (List Product)) -> (Model, Cmd Msg)
processGotProduct model res =
  case res of
    Ok prod -> ({ model | products = prod }, Cmd.none)
    Err err -> let _ = (Debug.log err) in ( model, Cmd.none )

-- view

view : Model -> Html msg
view model =
  div [] [text model.hello]

-- subscriptions

subscriptions : Model -> Sub Msg
subscriptions _ = Sub.none

-- http

getAllProduct: Cmd Msg
getAllProduct =
  Http.get
    { url = "/product"
    , expect = Http.expectJson GotProduct (Json.Decode.list productDecoder)
    }

productDecoder : Decoder Product
productDecoder =
  map4 Product
    (field "id" Json.Decode.string)
    (field "name" Json.Decode.string)
    (field "cost" Json.Decode.int)
    (field "description" Json.Decode.string)