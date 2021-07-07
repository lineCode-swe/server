#!/bin/bash

input_file=$1;

while IFS= read -r line; do
  # shellcheck disable=SC2086
  redis-cli $line
done < "$input_file"
