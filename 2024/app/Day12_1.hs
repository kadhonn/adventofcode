{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

{-# HLINT ignore "Eta reduce" #-}

module Day12_1
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
  let field = V.fromList . map V.fromList . lines $ input
      emptyOccupiedField = V.map (V.map (const False) ) field
      newState = State 0 0 emptyOccupiedField
      solvedState = solveState field newState
  print field
  print newState
  print solvedState

solveState :: Field Char -> State -> State
solveState _ s = s

data State = State
  { x :: Int,
    y :: Int,
    alreadyOccupiedMap :: Field Bool
  }
  deriving (Eq, Show)

type Field a = V.Vector (V.Vector a)