#!/bin/bash

input_file="map6x4.txt";

while IFS= read -r line; do
  # shellcheck disable=SC2086
  redis-cli $line
done < $input_file
