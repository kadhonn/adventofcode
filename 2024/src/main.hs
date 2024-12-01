import System.IO
import Day1_2

main = do
    contents <- readFile "../in.txt"
    print (run contents)
