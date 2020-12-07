

input <- read.delim(file = "in/r2.in", sep = " ", header = FALSE)
matrix <-  t(as.matrix(input))

View(matrix)

sum <- 0
for (i in 1:length(input$V1)) {
  tmp <- as.character(as.vector(input[i,]))
  tmp2 <- c()
  for(j in 1:length(tmp)){
    tmp2 <- c(tmp2, strsplit(tmp[j],"")[[1]])
  }
  tmp2 <- tmp2[!duplicated(tmp2)]
  tmp3 <- length(tmp2)
  sum <- sum + tmp3
  print(tmp2)
}
print(sum)