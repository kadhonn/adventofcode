import System.IO
import Day1_1

main = do
    contents <- readFile "../in.txt"
    print (run contents)
