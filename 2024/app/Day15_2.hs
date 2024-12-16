{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# HLINT ignore "Eta reduce" #-}
{-# OPTIONS_GHC -Wno-incomplete-uni-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

module Day15_2
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
      field = fromList . expand $ fieldLines
      moves = mconcat moveLines
      solvedFields = solve field moves
      result = calcResult (last solvedFields)
      printAll = mconcat . map printField $ solvedFields
  printField field
  print moves
--  printAll
  print result

expand field =
  let expandChar '#' = "##"
      expandChar '.' = ".."
      expandChar 'O' = "[]"
      expandChar '@' = "@."
      expandLine = mconcat . map expandChar
   in map expandLine field

calcResult :: Field Char -> Int
calcResult field =
  let width = length (V.head field)
      height = length field
      allBoxPrices = [y * 100 + x | x <- [0 .. width - 1], y <- [0 .. height - 1], getPoint field (x, y) == '[']
   in sum allBoxPrices

--solve field moves = foldl' moveField field moves
solve field moves = scanl' moveField field moves

moveField :: Field Char -> Char -> Field Char
moveField field move =
  let width = length (V.head field)
      height = length field
      robotPos = head [(x, y) | x <- [0 .. width - 1], y <- [0 .. height - 1], getPoint field (x, y) == '@']
      direction = getDirection move
      newField = movePos field robotPos direction
   in fromMaybe field newField

movePos :: Field Char -> Point -> Point -> Maybe (Field Char)
movePos field pos dir =
  let currentPosValue = getPoint field pos
      nextPos = addPoints pos dir
      setPosJust c pos field = Just (setPoint c field pos)
      (dx, dy) = dir
      (px, py) = pos
   in case currentPosValue of
        '.' -> Just field
        '#' -> Nothing
        '@' -> movePos field nextPos dir >>= setPosJust '@' nextPos >>= setPosJust '.' pos
        c ->
          if isHorizontal dir -- case of []
            then
              let (open, close) = if dx == 1 then ('[', ']') else (']', '[')
                  twoSteps = addPoints nextPos dir
               in movePos field twoSteps dir >>= setPosJust open nextPos >>= setPosJust close twoSteps
            else
              let (openPos, closePos) = if c == '[' then (pos, (px + 1, py)) else ((px - 1, py), pos)
                  (nextOpenPos, nextClosePos) = (addPoints openPos dir, addPoints closePos dir)
               in movePos field nextOpenPos dir
                    >>= (\newField -> movePos newField nextClosePos dir)
                    >>= setPosJust '[' nextOpenPos
                    >>= setPosJust ']' nextClosePos
                    >>= setPosJust '.' openPos
                    >>= setPosJust '.' closePos

isHorizontal (_, y) = y == 0

getDirection :: Char -> Point
getDirection '>' = (1, 0)
getDirection '<' = (-1, 0)
getDirection 'v' = (0, 1)
getDirection '^' = (0, -1)
getDirection _ = error "unknown move"