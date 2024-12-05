module Day4_1
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List
import Data.Maybe

run :: String -> IO ()
run input = do
  let field = lines input
      possibleStrings = getAllPossibleStrings field
      result = sum (map count possibleStrings)
  print possibleStrings
  print result

count :: String -> Int
count [] = 0
count ('X':'M':'A':'S':xs ) = 1 + count xs
count (_:xs) = count xs


getAllPossibleStrings :: [String] -> [String]
getAllPossibleStrings field =
  let rows = field
      columns = transpose field
      mainDiagonal = diagonal field
      antiDiagonal = diagonal $ map reverse field
      allForward = rows ++ columns ++ mainDiagonal ++ antiDiagonal
      allReverse = map reverse allForward
   in allForward ++ allReverse


diagonal :: [String] -> [String]
diagonal field = foldl appendRow (map (const []) (head field)) field

emptyListList = []:emptyListList


appendRow :: [String] -> String -> [String]
appendRow acu newRow = zipWith (++) ([]:acu) (map (: []) newRow++emptyListList)