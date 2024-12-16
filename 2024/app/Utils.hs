module Utils
  ( Field,
    Point,
    neighbours,
    setPoint,
    neighboursBounded,
    isInBounds,
    fromList,
    printField,
    getPoint,
    addPoints
  )
where

import Data.Char
import Data.List
import Data.Map qualified as M
import Data.Set qualified as Set
import Data.Vector qualified as V

type Field a = V.Vector (V.Vector a)

type Point = (Int, Int)

neighbours :: Point -> [Point]
neighbours (x, y) = [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

neighboursBounded :: Field a -> Point -> [Point]
neighboursBounded field (x, y) = filter (isInBounds field) [(x + 1, y), (x - 1, y), (x, y + 1), (x, y - 1)]

getPoint :: Field a -> Point -> a
getPoint f (x, y) = f V.! y V.! x

setPoint :: a -> Field a -> Point -> Field a
setPoint a field (x, y) = field V.// [(y, (field V.! y) V.// [(x, a)])]

addPoints :: Point -> Point -> Point
addPoints (x1, y1) (x2, y2) = (x1 + x2, y1 + y2)

isInBounds :: Field a -> Point -> Bool
isInBounds field (x, y) = x >= 0 && y >= 0 && y < length field && x < length (field V.! y)

fromList :: [[a]] -> Field a
fromList = V.fromList . map V.fromList

class FieldPrintable a where
  printForField :: a -> Char

instance FieldPrintable Char where
  printForField = id

instance FieldPrintable Int where
  printForField = head . show

printField :: (FieldPrintable a) => Field a -> IO ()
printField vector = do
  let field = V.toList (V.map V.toList vector)
      printLine = mconcat . map (putChar . printForField)
      printLines = map printLine field
      printLinesLn = map (`mappend` putStrLn "") printLines
  mconcat printLinesLn