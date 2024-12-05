module Day1_2
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List
import Data.Maybe

run input =
  let (left, right) = foldr splitLine ([], []) . lines $ input
      lookup = map (\l@(x : xs) -> (x, length l)) . group . sort $ right
   in  sum . map (lookupSimilarity lookup) $ left

lookupSimilarity :: [(Int, Int)] -> Int -> Int
lookupSimilarity lookup wanted =
  let result = find (\(nr, _) -> nr == wanted) lookup
   in wanted * snd ( fromMaybe (0, 0) result)

splitLine :: String -> ([Int], [Int]) -> ([Int], [Int])
splitLine line tuple =
  let splitted = words line
   in ((read $ splitted !! 0) : (fst tuple), (read $ splitted !! 1) : (snd tuple))
