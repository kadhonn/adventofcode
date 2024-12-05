{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day5_2
  ( run,
  )
where

import Data.Bifunctor
import Data.List
import Data.List.Split
import Data.Maybe

run :: String -> IO ()
run input = do
  let readInt = read :: String -> Int
      (rawRules, rawPages) = break ("" ==) (lines input)
      rules = map (bimap readInt readInt . fmap tail . break ('|' ==)) rawRules
      pages = map (map readInt . splitOn ",") $ tail rawPages
      wrongPages = filter (not . isValidPage rules) pages
      fixedPages = map (fixPage rules) wrongPages
      result = sum . map (\page -> page !! floor (toRational (length page `div` 2))) $ fixedPages
  -- print rules
  -- print pages
  print wrongPages
  print fixedPages
  print result

fixPage :: [(Int, Int)] -> [Int] -> [Int]
fixPage rules page =
  let actualRules = filter (\(l, r) -> elem l page && elem r page) rules
   in sortBy (ruleComparator actualRules) page

ruleComparator :: [(Int, Int)] -> Int -> Int -> Ordering
ruleComparator rules x y =
  let finder = (\x y rule -> (fst rule == x && snd rule == y) || (fst rule == y && snd rule == x))
      rule = fromJust . find (finder x y) $ rules
   in if x == y
        then
          EQ
        else
          if fst rule == x
            then
              LT
            else
              GT

isValidPage :: [(Int, Int)] -> [Int] -> Bool
isValidPage rules page = all (isValidRule page) rules

isValidRule :: [Int] -> (Int, Int) -> Bool
isValidRule page rule =
  let leftIndex = elemIndex (fst rule) page
      rightIndex = elemIndex (snd rule) page
   in case (leftIndex, rightIndex) of
        (Just left, Just right) -> left < right
        _ -> True