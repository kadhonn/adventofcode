

pub fn day1_1(str: String) {
    let mut max = 0;
    for elf in str.split("\r\n\r\n") {
        let mut calories = 0;
        for item in elf.split("\r\n") {
            if item.len() > 0 {
                calories += item.parse::<i32>().unwrap();
            }
        }
        if calories > max {
            max = calories;
        }
    }
    println!("{}", max);
}

pub fn day1_2(str: String) {
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
