{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

{-# HLINT ignore "Eta reduce" #-}

module Day11_1
  ( run,
  )
where

import Data.Char
import Data.List
import Data.Map qualified as M
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace

run :: String -> IO ()
run input = do
  let stones = map (read :: String -> Int) . words $ input
      result = blinkTimes M.empty 25 stones
  print stones
  print result

blinkTimes :: M.Map Int Int -> Int -> [Int] -> Int
blinkTimes cache times stones = sum . map (snd . blinkTimesStone cache times) $ stones

blinkTimesStone :: M.Map Int Int -> Int -> Int -> (M.Map Int Int, Int)
blinkTimesStone cache 0 _ = (cache, 1)
blinkTimesStone cache times stone =
--  case M.lookup stone cache of
--    Just r -> (cache, r)
--    Nothing ->
      let (cacher, nrr) =
            if stone == 0
              then
                blinkTimesStone cache (times - 1) 1
              else
                let nrAsString = show stone
                    l = length nrAsString
                    (left, right) = splitAt (l `div` 2) nrAsString
                 in if even l
                      then
                        let (cacheleft, resultleft) = blinkTimesStone cache (times - 1) (read left)
                            (cacheright, resultright) = blinkTimesStone cacheleft (times - 1) (read right)
                         in (cacheright, resultleft + resultright)
                      else
                        blinkTimesStone cache (times - 1) (stone * 2024)
       in (M.insert stone nrr cacher, nrr)