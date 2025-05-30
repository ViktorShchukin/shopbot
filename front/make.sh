#!/bin/bash

elm make ./src/Main.elm --output=out/elm.js

cp ./out/elm.js ../src/main/resources/static/elm.js