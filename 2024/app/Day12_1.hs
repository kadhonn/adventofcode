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
import Utils

run :: String -> IO ()
run input = do
  let field = V.fromList . map V.fromList . lines $ input
      emptyOccupiedField = V.map (V.map (const False)) field
      newState = State 0 0 emptyOccupiedField []
      solvedState@(State _ _ _ regions) = solveState field newState
      result = calcResult field regions
  print field
  print newState
  print solvedState
  print result

calcResult :: Field Char -> [Set.Set Point] -> Int
calcResult f = sum . map (calcSingleResult f)

calcSingleResult :: Field Char -> Set.Set Point -> Int
calcSingleResult f region =
  let area = length region
      borderFieldPairs = mconcat . map (\p -> map (p,) . filter (`notElem` region) . neighbours $ p) . Set.toList $ region
      borders = map toBorder borderFieldPairs
      borderMapInsert = M.insertWith (++)
      borderMap = foldl' (\m (f, t) -> borderMapInsert f [t] (borderMapInsert t [f] m)) M.empty borders
      (x, y) = head . Set.elems $ region
      regionValue = f V.! y V.! x
      sides = countSides f borderMap Set.empty regionValue
   in area * trace (show borderMap) (trace (show sides) sides)

countSides :: Field Char -> M.Map Point [Point] -> Set.Set Point -> Char -> Int
countSides f m alreadyVisited rV =
  let bordersLeft = filter (\(f, _) -> Set.notMember f alreadyVisited) . M.assocs $ m
   in if null bordersLeft
        then
          0
        else
          let (cf, ct1 : _) = head . filter (\(k, [t1, t2]) -> isHorizontal k t1 /= isHorizontal k t2) $ bordersLeft
              updatedSet = Set.insert cf (Set.insert ct1 alreadyVisited)
              (resultSet, sides) = countSidesR f rV m cf updatedSet cf ct1
           in sides + countSides f m resultSet rV

countSidesR :: Field Char -> Char -> M.Map Point [Point] -> Point -> Set.Set Point -> Point -> Point -> (Set.Set Point, Int)
countSidesR field rV m end alreadyVisited f t
  | t == end = (alreadyVisited, 1)
  | otherwise =
      let next = getNext field rV m f t
          isCorner = isHorizontal f t /= isHorizontal t next
          count = if isCorner then 1 else 0
          updatedSet = Set.insert next alreadyVisited
          (resultSet, result) = countSidesR field rV m end updatedSet t next
       in (resultSet, count + result)

getNext field rV m f t =
  let corner = filter (/= f) $ m M.! t
   in if length corner == 1
        then
          head corner
        else
          let ((x1, y1), (x2, y2)) = toRegion (f, t)
              correctPair = if field V.! y1 V.! x1 == rV then (x1, y1) else (x2, y2)
           in head . filter (\cP -> let (p1, p2) = toRegion (t, cP) in correctPair == p1 || correctPair == p2) $ corner

isHorizontal (x1, _) (x2, _) = x1 /= x2

toBorder :: (Point, Point) -> (Point, Point)
toBorder ((x1, y1), (x2, y2)) =
  if x1 == x2
    then
      ((x1, max y1 y2), (x1 + 1, max y1 y2))
    else
      ((max x1 x2, y1), (max x1 x2, y1 + 1))

toRegion :: (Point, Point) -> (Point, Point)
toRegion ((x1, y1), (x2, y2)) =
  if x1 == x2
    then
      ((x1, min y1 y2), (x1 - 1, min y1 y2))
    else
      ((min x1 x2, y1), (min x1 x2, y1 - 1))

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