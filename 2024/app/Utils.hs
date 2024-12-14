module Utils
  ( Field,
    Point,
    neighbours,
    setPoint,
    neighboursBounded,
    isInBounds,
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

setPoint :: a -> Field a -> Point -> Field a
setPoint a field (x, y) = field V.// [(y, (field V.! y) V.// [(x, a)])]

isInBounds :: Field a -> Point -> Bool
isInBounds field (x, y) = x >= 0 && y >= 0 && y < length field && x < length (field V.! y)