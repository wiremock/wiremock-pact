#!/bin/bash
script_dir="$( dirname -- "$0"; )";

rm $script_dir/*.png || echo No diagrams

npx plantuml-cli \
 -charset UTF-8 \
 $script_dir/*.puml

