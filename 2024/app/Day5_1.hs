{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day5_1
  ( run,
  )
where

import Data.Bifunctor
import Data.List
import Data.List.Split

run :: String -> IO ()
run input = do
  let readInt = read :: String -> Int
      (rawRules, rawPages) = break ("" ==) (lines input)
      rules = map (bimap readInt readInt . fmap tail . break ('|' ==)) rawRules
      pages = map (map readInt . splitOn ",") $ tail rawPages
      correctPages = filter (isValidPage rules) pages
      result = sum . map (\page -> page !! floor (toRational (length page `div` 2))) $ correctPages
  -- print rules
  -- print pages
  print correctPages
  print result

isValidPage :: [(Int, Int)] -> [Int] -> Bool
isValidPage rules page = all (isValidRule page) rules

isValidRule :: [Int] -> (Int, Int) -> Bool
isValidRule page rule =
  let leftIndex = elemIndex (fst rule) page
      rightIndex = elemIndex (snd rule) page
   in case (leftIndex, rightIndex) of
        (Just left, Just right) -> left < right
        _ -> True