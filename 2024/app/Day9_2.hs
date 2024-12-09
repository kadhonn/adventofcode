{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day9_2
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
      filesAndSpacesList = filter (\(_, c, _) -> c > 0) $ extractAll 0 filesAndSpacesInts
      filesAndSpaces = V.fromList filesAndSpacesList
      defragmentFilesAndSpaces = defragment filesAndSpaces
      sum = sumUp 0 (V.head defragmentFilesAndSpaces) (V.tail defragmentFilesAndSpaces)
  --  print filesList
  print filesAndSpaces
  print defragmentFilesAndSpaces
  print sum

sumUp pos (isFile, c, id) list
  | c == 0 = if V.null list then 0 else sumUp pos (V.head list) (V.tail list)
  | otherwise =
      let curSum = if isFile then pos * id else 0
       in curSum + sumUp (pos + 1) (isFile, c - 1, id) list

defragment :: V.Vector (Bool, Int, Int) -> V.Vector (Bool, Int, Int)
defragment list = defragmentR (length list - 1) list

defragmentR 0 list = list
defragmentR i list =
  let file@(isFile, count, id) = list V.! i
   in if not isFile
        then
          defragmentR (i - 1) list
        else
          let maybeInsertCount = searchInsertIndex i list 0 count
           in case maybeInsertCount of
                Nothing -> defragmentR (i - 1) list
                Just insertIndex ->
                  let removedOldList = list V.// [(i, (False, count, id))]
                      (vhead, vtail) = V.splitAt insertIndex removedOldList
                      (sisFile, sc, sid) = V.head vtail
                      toInsert
                        | sisFile || sid /= -1 = error "wtf"
                        | sc == count = [file]
                        | otherwise = [file, (False, sc - count, -1)]
                      newList = vhead V.++ (V.fromList toInsert) V.++ (V.tail vtail)
                   in defragmentR (i - (2 -(length toInsert))) newList

searchInsertIndex :: Int -> V.Vector (Bool, Int, Int) -> Int -> Int -> Maybe Int
searchInsertIndex endI _ curI _
  | endI == curI = Nothing
searchInsertIndex endI list curI wantedCount =
  let (isFile, c, _) = list V.! curI
      continue = searchInsertIndex endI list (curI + 1) wantedCount
   in if isFile
        then
          continue
        else
          if c >= wantedCount
            then
              Just curI
            else
              continue

extractAll :: Int -> [Int] -> [(Bool, Int, Int)]
extractAll _ [] = []
extractAll fid [f] = [(True, f, fid)]
extractAll fid (f : s : xs) = (True, f, fid) : (False, s, -1) : extractAll (fid + 1) xs

-- sumUp pos (isFile, c, id) list =
--  let bla = if isFile then (sum [1 .. (c - 1)] + pos * c) * id else 0
--      curSum = traceShow bla bla
--   in if V.null list
--        then
--          curSum
--        else
--          curSum + sumUp (pos + c) (V.head list) (V.tail list)