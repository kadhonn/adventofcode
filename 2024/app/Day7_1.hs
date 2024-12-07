{-# OPTIONS_GHC -Wno-incomplete-patterns #-}
{-# OPTIONS_GHC -Wno-missing-signatures #-}

module Day7_1
  ( run,
  )
where

run :: String -> IO ()
run input = do
  let rawEquations = map words (lines input)
      equations :: [(Int, [Int])] = map (\e -> (read . init $ head e, map read (tail e))) rawEquations
      solved = filter canBeSolved equations
  print solved
  print . sum . map fst $ solved

canBeSolved :: (Int, [Int]) -> Bool
canBeSolved (s, nr : nrs) = canBeSolvedR s nr nrs

canBeSolvedR :: Int -> Int -> [Int] -> Bool
canBeSolvedR s r [] = s == r
canBeSolvedR s r (nr : nrs) = canBeSolvedR s (r * nr) nrs || canBeSolvedR s (r + nr) nrs