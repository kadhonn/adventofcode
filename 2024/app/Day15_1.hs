{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# HLINT ignore "Eta reduce" #-}
{-# OPTIONS_GHC -Wno-incomplete-uni-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

module Day15_1
  ( run,
  )
where

import Control.Concurrent
import Data.Char
import Data.List
import Data.Map qualified as M
import Data.Maybe
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace
import System.IO
import Text.Regex
import Utils

run :: String -> IO ()
run input = do
  let (fieldLines, _ : moveLines) = span (/= "") . lines $ input
      field = fromList fieldLines
      moves = mconcat moveLines
      solvedField = solve field moves
      result = calcResult solvedField
  printField field
  print moves
  printField solvedField
  print result

calcResult :: Field Char -> Int
calcResult field =
  let width = length (V.head field)
      height = length field
      allBoxPrices = [y * 100 + x | x <- [0 .. width - 1], y <- [0 .. height - 1], getPoint field (x, y) == 'O']
   in sum allBoxPrices

solve field moves = foldl' moveField field moves

moveField :: Field Char -> Char -> Field Char
moveField field move =
  let width = length (V.head field)
      height = length field
      robotPos = head [(x, y) | x <- [0 .. width - 1], y <- [0 .. height - 1], getPoint field (x, y) == '@']
      direction = getDirection move
      newField = do
        boxPos <- getNewBoxPos field robotPos direction
        moveNewBoxPos field robotPos direction boxPos
   in fromMaybe field newField

getNewBoxPos :: Field Char -> Point -> Point -> Maybe Point
getNewBoxPos field robotPos direction =
  let nextPos = addPoints robotPos direction
      nextPosValue = getPoint field nextPos
   in case nextPosValue of
        '.' -> Just nextPos
        '#' -> Nothing
        _ -> getNewBoxPos field nextPos direction

moveNewBoxPos :: Field Char -> Point -> Point -> Point -> Maybe (Field Char)
moveNewBoxPos field robotPos direction newBoxPos =
  let f1 = setPoint 'O' field newBoxPos
      f2 = setPoint '.' f1 robotPos
      f3 = setPoint '@' f2 (addPoints robotPos direction)
   in Just f3

getDirection :: Char -> Point
getDirection '>' = (1, 0)
getDirection '<' = (-1, 0)
getDirection 'v' = (0, 1)
getDirection '^' = (0, -1)
getDirection _ = error "unknown move"