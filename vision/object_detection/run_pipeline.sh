#!/usr/bin/env bash

export $(grep -v '#.*' .env | xargs)

dvc exp run