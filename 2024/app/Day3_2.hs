module Day3_2
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List
import Data.Maybe

run :: String -> IO ()
run input = do
  let result = calc input True
  print result

calc [] _ = 0
calc ('d' : 'o' : 'n' : '\'' : 't' : '(' : ')' : xs) True = calc xs False
calc ('d' : 'o' : '(' : ')' : xs) False = calc xs True
calc ('m' : 'u' : 'l' : '(' : xs) True =
  let firstNumberMaybe = getNumber xs
   in case firstNumberMaybe of
        Nothing -> calc xs True
        (Just (firstNumber, xs)) -> case checkChar ',' xs of
          Nothing -> calc xs True
          (Just xs) ->
            let secondNumberMaybe = getNumber xs
             in case secondNumberMaybe of
                  Nothing -> calc xs True
                  (Just (secondNumber, xs)) -> case checkChar ')' xs of
                    Nothing -> calc xs True
                    (Just xs) -> firstNumber * secondNumber + calc xs True
calc (_ : xs) e = calc xs e

checkChar :: Char -> String -> Maybe String
checkChar _ [] = Nothing
checkChar c (x : xs)
  | c == x = Just xs
  | otherwise = Nothing

getNumber :: String -> Maybe (Int, String)
getNumber str =
  let number = getNumberR str
   in case number of
        ([], _) -> Nothing
        (nr, s) ->
          if length nr < 1 || length nr > 3
            then
              Nothing
            else
              Just (read nr, s)

getNumberR :: String -> (String, String)
getNumberR [] = ([], [])
getNumberR (s : xs)
  | isDigit s =
      let (s', xs') = getNumberR xs
       in (s : s', xs')
  | otherwise = ([], s : xs)
