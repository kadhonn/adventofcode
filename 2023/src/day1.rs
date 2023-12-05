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
    let mut calories_vec: Vec<i32> = vec!();
    for elf in str.split("\r\n\r\n") {
        let mut calories = 0;
        for item in elf.split("\r\n") {
            if item.len() > 0 {
                calories += item.parse::<i32>().unwrap();
            }
        }
        calories_vec.push(calories);
    }
    calories_vec.sort();
    let mut sum = 0;
    for i in 0..3 {
        sum += calories_vec[calories_vec.len() - 1 - i];
    }
    println!("{}", sum);
}
