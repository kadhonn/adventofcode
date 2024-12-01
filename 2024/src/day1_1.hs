module Day1_1
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List

run = sum . map (abs . uncurry (-)) . zipList . sortTuple . foldr splitLine ([], []) . lines


zipList (x, x1) = zip x x1

sortTuple (x, x1) = (sort x, sort x1)

splitLine :: String -> ([Int], [Int]) -> ([Int], [Int])
splitLine line tuple =  let
                            splitted = words line
                        in
                            ((read $ splitted !! 0) : (fst tuple), (read $ splitted !! 1) : (snd tuple))
