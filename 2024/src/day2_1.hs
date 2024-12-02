module Day2_1
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List
import Data.Maybe

run :: String -> IO ()
run input = do
  let inputLines = lines input
      stringReports = map words inputLines
      reports :: [[Int]] = map (map read) stringReports
      safeReports = filter isSafeReport reports
  print reports
  print safeReports
  print (length safeReports)

isSafeReport :: [Int] -> Bool
isSafeReport levels = isAllDecreasing levels || isAllIncreasing levels

isAllDecreasing (x : xs) =
  let result = foldl (\(lastNumber, lastResult) currentNumber -> (currentNumber, lastResult && lastNumber - currentNumber >= 1 && lastNumber - currentNumber <= 3)) (x, True) xs
   in snd result

isAllIncreasing (x : xs) =
  let result = foldl (\(lastNumber, lastResult) currentNumber -> (currentNumber, lastResult && currentNumber - lastNumber >= 1 && currentNumber - lastNumber <= 3)) (x, True) xs
   in snd result