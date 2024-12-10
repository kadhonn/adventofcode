{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

{-# HLINT ignore "Eta reduce" #-}

module Day10_1
  ( run,
  )
where

import Data.Char
import Data.List
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace

run :: String -> IO ()
run input = do
  let m = V.fromList . map (V.fromList . map digitToInt) . lines $ input
      l = length (V.head m)
      h = length m
      trailheads = [(x, y) | x <- [0 .. l - 1], y <- [0 .. h - 1], m V.! y V.! x == 0]
      pathsForTrailheads = findPaths m trailheads
      result = sum . map (length . group . sort) $ pathsForTrailheads
  printField m
  print (zip [0 ..] trailheads)
  print (zip [0 ..] pathsForTrailheads)
  print result

findPaths :: V.Vector (V.Vector Int) -> [(Int, Int)] -> [[(Int, Int)]]
findPaths m trailheads = foldl' (\a th -> findPath m th : a) [] trailheads

findPath :: V.Vector (V.Vector Int) -> (Int, Int) -> [(Int, Int)]
findPath m trailhead = findPathR m trailhead

findPathR :: V.Vector (V.Vector Int) -> (Int, Int) -> [(Int, Int)]
findPathR m (x, y) =
  let curElevation = m V.! y V.! x
   in if curElevation == 9
        then
          [(x, y)]
        else
          let l = length (V.head m)
              h = length m
              possibleWays = filter (\(x, y) -> x >= 0 && x < l && y >= 0 && y < h && m V.! y V.! x == curElevation + 1) [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]
           in mconcat . map (findPathR m) $ possibleWays

printField :: (Show a) => V.Vector (V.Vector a) -> IO ()
printField vector = do
  let field = V.toList (V.map V.toList vector)
      printLine = mconcat . map (putStr . show)
      printLines = map printLine field
      printLinesLn = map (`mappend` putStrLn "") printLines
  mconcat printLinesLn