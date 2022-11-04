#!/usr/bin/env bash

export $(grep -v '#.*' .env | xargs)

dvc repro -s train
dvc repro -s val
