{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day8_2
  ( run,
  )
where

import Data.List
import Data.Set qualified as Set

run :: String -> IO ()
run input = do
  let field = lines input
      maxX = length (head field)
      maxY = length field
      antennas = [(x, y, field !! y !! x) | x <- [0 .. maxX - 1], y <- [0 .. maxY - 1], field !! y !! x /= '.']
      groupedAntennas = groupBy (\(_, _, freq1) (_, _, freq2) -> freq1 == freq2) . sortOn (\(_, _, freq1) -> freq1) $ antennas
      antiNodes = calcAntiNodes maxX maxY groupedAntennas

  print groupedAntennas
  print antiNodes
  print $ Set.size antiNodes

calcAntiNodes :: Int -> Int -> [[(Int, Int, Char)]] -> Set.Set (Int, Int)
calcAntiNodes maxX maxY =
  let isInBounds (x, y) = x >= 0 && x < maxX && y >= 0 && y < maxY
   in Set.fromList . foldl' (\a l -> a ++ takeWhile isInBounds l) [] . foldl' (\s group -> s ++ calcAntiNodesSingle group) []

calcAntiNodesSingle :: [(Int, Int, Char)] -> [[(Int, Int)]]
calcAntiNodesSingle group =
  let allPairs = [(p1, p2) | p1 <- group, p2 <- group, p1 /= p2]
   in foldl' (++) [] $ map getAntiNodes allPairs

getAntiNodes :: ((Int, Int, Char), (Int, Int, Char)) -> [[(Int, Int)]]
getAntiNodes ((x1, y1, _), (x2, y2, _)) =
  let diffX = x2 - x1
      diffY = y2 - y1
   in [map (\i -> (x1 - diffX * i, y1 - diffY * i)) [0 ..], map (\i -> (x2 + diffX * i, y2 + diffY * i)) [0 ..]]