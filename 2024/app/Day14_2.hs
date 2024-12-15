{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# HLINT ignore "Eta reduce" #-}
{-# OPTIONS_GHC -Wno-incomplete-uni-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

module Day14_2
  ( run,
  )
where

import Data.Char
import Data.List
import Data.Map qualified as M
import Data.Maybe
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace
import Text.Regex
import Utils
import System.IO
import Control.Concurrent

width = 101 :: Int

height = 103 :: Int

run :: String -> IO ()
run input = do
--  hSetBuffering stdout NoBuffering
--  hSetBuffering stdin NoBuffering
  let robots = parseInput (lines input)
  print robots
  runLoop 1 robots

runLoop :: Int -> [Robot] -> IO ()
runLoop 10000 _ = return ()
runLoop c robots = do
  let newRobots = moveRobots 1 robots
  print c
  printRobots newRobots
--  _ <- getLine
--  threadDelay 100
  runLoop (c + 1) newRobots


isSimilarSafety :: [Robot] -> Bool
isSimilarSafety robots =
  let (ul, ur, ll, lr) = foldl' safetyFold (0, 0, 0, 0) robots
   in ul == ur && ll == lr

safetyFold (ul, ur, ll, lr) (Robot (px, py) _) =
  let isLeft = px < (width `div` 2)
      isRight = px > (width `div` 2)
      isUpper = py < (height `div` 2)
      isLower = py > (height `div` 2)
   in if isLeft && isUpper
        then (ul + 1, ur, ll, lr)
        else
          if isRight && isUpper
            then (ul, ur + 1, ll, lr)
            else
              if isLeft && isLower
                then (ul, ur, ll + 1, lr)
                else
                  if isRight && isLower
                    then (ul, ur, ll, lr + 1)
                    else
                      (ul, ur, ll, lr)

printRobots :: [Robot] -> IO ()
printRobots robots =
  let printLine fy = mconcat . map (\fx -> if any (\(Robot (px, py) _) -> px == fx && py == fy) robots then putStr "#" else putStr ".") $ [0 .. width - 1]
      printLines = mconcat . map (\y -> printLine y <> putStrLn "") $ [0 .. height - 1]
   in printLines

moveRobots :: Int -> [Robot] -> [Robot]
moveRobots times robots = foldl' (\r _ -> map moveRobot r) robots [1 .. times]

moveRobot :: Robot -> Robot
moveRobot (Robot (px, py) (vx, vy)) =
  let newPx = (px + vx + width) `mod` width
      newPy = (py + vy + height) `mod` height
   in Robot (newPx, newPy) (vx, vy)

parseInput :: [String] -> [Robot]
parseInput = map parseRobot

parseRobot robot =
  let robotRegex = mkRegex "p=([-]?[[:digit:]]+),([-]?[[:digit:]]+) v=([-]?[[:digit:]]+),([-]?[[:digit:]]+)"
      (_, _, _, px : py : vx : vy : _) = fromJust (matchRegexAll robotRegex robot)
   in Robot (read px, read py) (read vx, read vy)

data Robot = Robot
  { p :: Point,
    v :: Point
  }
  deriving (Eq, Show)