{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day6_1
  ( run,
  )
where

import Data.List
import Data.Set qualified as Set

run :: String -> IO ()
run input = do
  let rawField = lines input
      (field, guard) = findAndRemoveGuard rawField
      guardPath = guardWalk field guard
  printField field
  print guard
  print guardPath
  print $ length (Set.fromList guardPath)

guardWalk :: [String] -> (Int, Int, Char) -> [(Int, Int)]
guardWalk field (x, y, dir) =
  let (newX, newY) = newPos x y dir
      isInBounds = newX >= 0 && newX < length (head field) && newY >= 0 && newY < length field
   in if not isInBounds
        then
          [(x, y)]
        else
          let isBlocked = field !! newY !! newX == '#'
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

newPos x y dir = case dir of
  'v' -> (x, y + 1)
  '^' -> (x, y - 1)
  '>' -> (x + 1, y)
  '<' -> (x - 1, y)

findAndRemoveGuard :: [String] -> ([String], (Int, Int, Char))
findAndRemoveGuard field =
  let (x, y) = head ([(x, y) | x <- [0 .. length (head field) - 1], y <- [0 .. length field - 1], field !! y !! x `elem` guardDirections])
      newField = map (map (\c -> if c `elem` guardDirections then '.' else c)) field
   in (newField, (x, y, field !! y !! x))

guardDirections = "v^<>"

printField :: [String] -> IO ()
printField field = do
  let printLine = mconcat . map putChar
      printLines = map printLine field
      printLinesLn = map (`mappend` putStrLn "") printLines
  mconcat printLinesLn