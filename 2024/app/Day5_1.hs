{-# OPTIONS_GHC -Wno-missing-signatures #-}
module Day5_1
  ( run,
  )
where

import Data.Bifunctor
import Data.List.Split

run :: String -> IO ()
run input = do
  let (rawRules, rawPages) = break ("" ==) (lines input)
      rules = map (bimap readInt readInt . fmap tail . break ('|' ==)) rawRules
      pages = map (map readInt . splitOn ",") $ tail rawPages
  print rules
  print pages

readInt = read :: String -> Int