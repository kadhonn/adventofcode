{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}
{-# OPTIONS_GHC -Wno-unrecognised-pragmas #-}

{-# HLINT ignore "Eta reduce" #-}

module Day12_2
  ( run,
  )
where

import Data.Char
import Data.List
import Data.Map qualified as M
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace
import GHC.Real (FractionalExponentBase (Base2))
import Utils

run :: String -> IO ()
run input = do
  let field = V.fromList . map V.fromList . lines $ input
      emptyOccupiedField = V.map (V.map (const False)) field
      newState = State 0 0 emptyOccupiedField []
      solvedState@(State _ _ _ regions) = solveState field newState
      result = calcResult regions
  print field
  print newState
  print solvedState
  print result

calcResult :: [Set.Set Point] -> Int
calcResult = sum . map calcSingleResult

calcSingleResult :: Set.Set Point -> Int
calcSingleResult region =
  let area = length region
      borderFieldPairs = mconcat . map (\p -> map (p,) . filter (`notElem` region) . neighbours $ p) . Set.toList $ region
      borders = map toBorder borderFieldPairs
      borderMapInsert = M.insertWith (++)
      borderMap = foldl' (\m (f, t) -> borderMapInsert f [t] (borderMapInsert t [f] m)) M.empty borders
      sides = countSides borderMap
   in area * trace (show borderMap) (trace (show sides) sides)

countSides :: M.Map Point [Point] -> Int
countSides = sum . map cornerCount . M.assocs

cornerCount :: (Point, [Point]) -> Int
cornerCount (f, [t1, t2]) = if isHorizontal f t1 == isHorizontal f t2 then 0 else 1
cornerCount (_, l)
  | length l == 4 = 2
  | otherwise = error "wat"

isHorizontal (x1, _) (x2, _) = x1 /= x2

toBorder :: (Point, Point) -> (Point, Point)
toBorder ((x1, y1), (x2, y2)) =
  if x1 == x2
    then
      ((x1, max y1 y2), (x1 + 1, max y1 y2))
    else
      ((max x1 x2, y1), (max x1 x2, y1 + 1))

solveState :: Field Char -> State -> State
solveState field s@(State x y alreadyOccupiedMap regions)
  | y >= length field = s
  | x >= length (field V.! y) = solveState field (State 0 (y + 1) alreadyOccupiedMap regions)
  | otherwise =
      let currentPoint = (x, y)
       in if alreadyOccupiedMap V.! y V.! x
            then
              solveState field (State (x + 1) y alreadyOccupiedMap regions)
            else
              let newRegion = floodRegion field currentPoint Set.empty
                  newAlreadyOccupiedMap = foldl' (setPoint True) alreadyOccupiedMap newRegion
               in solveState field (State (x + 1) y newAlreadyOccupiedMap (newRegion : regions))

floodRegion :: Field Char -> Point -> Set.Set Point -> Set.Set Point
floodRegion field p@(x, y) set =
  let currentRegion = field V.! y V.! x
      allNeighbours = neighboursBounded field p
      matchingNeighbours = filter (\n@(nx, ny) -> field V.! ny V.! nx == currentRegion && not (Set.member n set)) allNeighbours
      newSet = Set.insert p set
   in foldl' (\s n -> Set.union s (floodRegion field n s)) newSet matchingNeighbours

data State = State
  { x :: Int,
    y :: Int,
    alreadyOccupiedMap :: Field Bool,
    regions :: [Set.Set Point]
  }
  deriving (Eq, Show)