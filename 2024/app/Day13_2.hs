{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# HLINT ignore "Eta reduce" #-}
{-# OPTIONS_GHC -Wno-incomplete-uni-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

module Day13_2
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

run :: String -> IO ()
run input = do
  let maschines = parseInput (lines input)
      prizes = map winMaschine maschines
      result = sum . map (fromMaybe 0) $ prizes
  print maschines
  print prizes
  print result

winMaschine :: Maschine -> Maybe Int
winMaschine (Maschine (xa, ya) (xb, yb) (xp, yp)) =
  let aP1 = (xp * yb - yp * xb)
      aP2 = (xa * yb - ya * xb)
   in if mod aP1 aP2 /= 0
        then Nothing
        else
          let a = aP1 `div` aP2
              bP1 = (yp - a * ya)
              bP2 = yb
           in if mod bP1 bP2 /= 0
                then Nothing
                else Just (a * 3 + (bP1 `div` bP2))

parseInput :: [String] -> [Maschine]
parseInput [] = []
parseInput lines =
  let (a : b : prize : _, rest) = splitAt 4 lines
   in parseMaschine a b prize : parseInput rest

parseMaschine a b prize =
  let buttonARegex = mkRegex "Button A\\: X\\+([[:digit:]]+), Y\\+([[:digit:]]+)"
      buttonBRegex = mkRegex "Button B\\: X\\+([[:digit:]]+), Y\\+([[:digit:]]+)"
      prizeRegex = mkRegex "Prize\\: X\\=([[:digit:]]+), Y=([[:digit:]]+)"
      (_, _, _, xa : ya : _) = fromJust (matchRegexAll buttonARegex a)
      (_, _, _, xb : yb : _) = fromJust (matchRegexAll buttonBRegex b)
      (_, _, _, xp : yp : _) = fromJust (matchRegexAll prizeRegex prize)
   in Maschine (read xa, read ya) (read xb, read yb) (read xp + 10000000000000, read yp + 10000000000000)

data Maschine = Maschine
  { buttonA :: Point,
    buttonB :: Point,
    prize :: Point
  }
  deriving (Eq, Show)