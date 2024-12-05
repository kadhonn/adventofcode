module Day4_2
  ( run,
  )
where

import Data.Bifunctor
import Data.Char (isDigit)
import Data.List
import Data.Maybe

run :: String -> IO ()
run input = do
  let field = lines input
      maxX = (length . head) field - 1
      maxY = length field - 1
      allALocations = [(x, y) | x <- [0 .. maxX], y <- [0 .. maxY], field !! y !! x == 'A', x > 0, x < maxX, y > 0, y < maxY]
      allValidLocations = filter (isValidXMas field) allALocations
  print allValidLocations
  print (length allValidLocations)

isValidXMas :: [String] -> (Int, Int) -> Bool
isValidXMas field pair =
  let mainDiagonal = diagonal field pair [(0, 0), (1, 1), (2, 2)]
      antiDiagonal = diagonal field pair [(2, 0), (1, 1), (0, 2)]
      allDiagonals = [mainDiagonal, antiDiagonal, reverse mainDiagonal, reverse antiDiagonal]
      matchingDiagonals = filter ("MAS" ==) allDiagonals
   in length matchingDiagonals == 2

diagonal :: [String] -> (Int, Int) -> [(Int, Int)] -> String
diagonal field pair coords =
  let absoluteCoords = map (\(x, y) -> bimap (x -1  +) (y -1  +) pair) coords
      resolved = map (\(x, y) -> field !! y !! x) absoluteCoords
   in resolved
