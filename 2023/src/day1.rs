pub fn day1_1(str: &str) {
    let mut sum = 0;
    let mut first: i32;
    let mut last: i32;
    for line in str.split("\n") {
        if line.is_empty() {
            continue;
        }
        first = -1;
        last = -1;
        for char in line.chars() {
            if char.is_digit(10) {
                let digit = char.to_digit(10).unwrap() as i32;
                if first == -1 {
                    first = digit
                }
                last = digit
            }
        }
        sum += first * 10 + last
    }
    println!("{}", sum)
}

pub fn day1_2(str: &str) {
    let mut sum = 0;
    let mut first: i32;
    let mut last: i32;
    for line in str.split("\n") {
        if line.is_empty() {
            continue;
        }
        first = -1;
        last = -1;
        for (i, char) in line.char_indices() {
            let mut digit: i32 = -1;
            if char.is_digit(10) {
                digit = char.to_digit(10).unwrap() as i32;
            }
            if line[i..line.len()].starts_with("one") {
                digit = 1
            }
            if line[i..line.len()].starts_with("two") {
                digit = 2
            }
            if line[i..line.len()].starts_with("three") {
                digit = 3
            }
            if line[i..line.len()].starts_with("four") {
                digit = 4
            }
            if line[i..line.len()].starts_with("five") {
                digit = 5
            }
            if line[i..line.len()].starts_with("six") {
                digit = 6
            }
            if line[i..line.len()].starts_with("seven") {
                digit = 7
            }
            if line[i..line.len()].starts_with("eight") {
                digit = 8
            }
            if line[i..line.len()].starts_with("nine") {
                digit = 9
            }
            if digit != -1 {
                if first == -1 {
                    first = digit
                }
                last = digit
            }
        }
        sum += first * 10 + last
    }
    println!("{}", sum)
}
