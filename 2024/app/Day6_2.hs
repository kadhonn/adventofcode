{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day6_2
  ( run,
  )
where

import Data.List
import Data.HashSet qualified as Set
import Data.Vector qualified as V

run :: String -> IO ()
run input = do
  let rawField = lines input
      (fieldAsList, guard@(gx, gy, _)) = findAndRemoveGuard rawField
      field = V.fromList (map V.fromList fieldAsList)
      originalWalk = guardWalk field guard
      allObstacles = Set.fromList [(x, y) | (x, y) <- originalWalk, not (x == gx && y == gy)]
      isCircle o = guardWalkInCircle field o guard Set.empty
      validObstacles = filter isCircle (Set.toList allObstacles)
  printField field
  print guard
--  print validObstacles
  print (length validObstacles)

guardWalkInCircle :: V.Vector (V.Vector Char) -> (Int, Int) -> (Int, Int, Char) -> Set.HashSet (Int, Int, Char) -> Bool
guardWalkInCircle field o@(ox, oy) (x, y, dir) path =
  let (newX, newY) = case dir of
                       'v' -> (x, y + 1)
                       '^' -> (x, y - 1)
                       '>' -> (x + 1, y)
                       '<' -> (x - 1, y)
      isInBounds = newX >= 0 && newX < length (V.head field) && newY >= 0 && newY < length field
   in ( Set.member (x, y, dir) path
          || ( isInBounds
                 && ( let isBlocked = checkField field newX newY || (newX == ox && newY == oy)
                       in if not isBlocked
                            then
                              guardWalkInCircle field o (newX, newY, dir) path
                            else
                              let newDir = case dir of
                                    'v' -> '<'
                                    '<' -> '^'
                                    '^' -> '>'
                                    '>' -> 'v'
                               in guardWalkInCircle field o (x, y, newDir) (Set.insert (x, y, dir) path)
                    )
             )
      )

checkField field newX newY = field V.! newY V.! newX == '#'

findAndRemoveGuard :: [String] -> ([String], (Int, Int, Char))
findAndRemoveGuard field =
  let (x, y) = head ([(x, y) | x <- [0 .. length (head field) - 1], y <- [0 .. length field - 1], field !! y !! x `elem` guardDirections])
      newField = map (map (\c -> if c `elem` guardDirections then '.' else c)) field
   in (newField, (x, y, field !! y !! x))

guardDirections = "v^<>"

printField :: V.Vector (V.Vector Char) -> IO ()
printField vector = do
  let field = V.toList (V.map V.toList vector)
      printLine = mconcat . map putChar
      printLines = map printLine field
      printLinesLn = map (`mappend` putStrLn "") printLines
  mconcat printLinesLn

guardWalk :: V.Vector (V.Vector Char) -> (Int, Int, Char) -> [(Int, Int)]
guardWalk field (x, y, dir) =
  let (newX, newY) = case dir of
                       'v' -> (x, y + 1)
                       '^' -> (x, y - 1)
                       '>' -> (x + 1, y)
                       '<' -> (x - 1, y)
      isInBounds = newX >= 0 && newX < length (V.head field) && newY >= 0 && newY < length field
   in if not isInBounds
        then
          [(x, y)]
        else
          let isBlocked = field V.! newY V.! newX == '#'
           in if not isBlocked
                then
                  (x, y) : guardWalk field (newX, newY, dir)
                else
                  let newDir = case dir of
                        'v' -> '<'
                        '<' -> '^'
                        '^' -> '>'
                        '>' -> 'v'
                   in guardWalk field (x, y, newDir)