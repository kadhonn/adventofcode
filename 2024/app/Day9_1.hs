{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day9_1
  ( run,
  )
where

import Data.Char
import Data.List
import Data.Set qualified as Set
import Data.Vector qualified as V
import Debug.Trace

run :: String -> IO ()
run input = do
  let filesAndSpacesInts = map digitToInt input
      filesList = filter (\(_, c) -> c > 0) $ extractFiles 0 filesAndSpacesInts
      filesAndSpacesList = filter (\(_, c) -> c > 0) $ extractAll filesAndSpacesInts
      files = V.fromList filesList
      filesAndSpaces = V.fromList filesAndSpacesList
      sum = sumUp 0 filesAndSpaces files
--  print filesList
  print sum

sumUp :: Int -> V.Vector (Bool, Int) -> V.Vector (Int, Int) -> Int
sumUp _ _ files
  | V.null files = 0
sumUp place filesAndSpaces files =
  let (isFile, c) = V.head filesAndSpaces
      (fOp, fRemoveOp, fIndex) = if isFile then (V.head, V.tail, 0) else (V.last, V.init, V.length files - 1)
      (fileId, fileCount) = fOp files
      fileValue = place * fileId
      newFilesAndSpaces = if c == 1 then V.tail filesAndSpaces else filesAndSpaces V.// [(0, (isFile, c - 1))]
      newFiles = if fileCount == 1 then fRemoveOp files else files V.// [(fIndex, (fileId, fileCount - 1))]
   in fileValue + sumUp (place + 1) newFilesAndSpaces newFiles

extractFiles :: Int -> [Int] -> [(Int, Int)]
extractFiles _ [] = []
extractFiles c [f] = [(c, f)]
extractFiles c (f : _ : xs) = (c, f) : extractFiles (c + 1) xs

extractAll :: [Int] -> [(Bool, Int)]
extractAll [] = []
extractAll [f] = [(True, f)]
extractAll (f : s : xs) = (True, f) : (False, s) : extractAll xs
