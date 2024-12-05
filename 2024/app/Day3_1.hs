module Day3_1
  ( run,
  )
where

import Data.Char (isDigit)
import Data.List
import Data.Maybe

run :: String -> IO ()
run input = do
  let result = calc input
  print result

calc [] = 0
calc ('m' : 'u' : 'l' : '(' : xs) =
  let firstNumberMaybe = getNumber xs
   in case firstNumberMaybe of
        Nothing -> calc xs
        (Just (firstNumber, xs)) -> case checkChar ',' xs of
          Nothing -> calc xs
          (Just xs) ->
            let secondNumberMaybe = getNumber xs
             in case secondNumberMaybe of
                  Nothing -> calc xs
                  (Just (secondNumber, xs)) -> case checkChar ')' xs of
                    Nothing -> calc xs
                    (Just xs) -> firstNumber * secondNumber + calc xs
calc (_ : xs) = calc xs

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
        (nr, s) -> if length nr < 1 || length nr > 3 then
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
