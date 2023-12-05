use std::cmp::{max, min};

pub fn day3_1(str: &str) {
    let mut sum = 0;
    let field = str.lines().map(|line| line.chars().collect::<Vec<char>>()).collect::<Vec<Vec<char>>>();

    for y in 0..field.len() {
        let mut number = -1;
        let mut found_symbol = false;
        for x in 0..field[y].len() {
            let char = field[y][x];
            if char.is_digit(10) {
                let digit = char.to_digit(10).unwrap() as i32;
                if number == -1 {
                    number = digit;
                } else {
                    number = number * 10 + digit;
                }
                for i in max(0, y as i32 - 1)..=min(field.len() as i32 - 1, y as i32 + 1) {
                    for j in max(0, x as i32 - 1)..=min(field[i as usize].len() as i32 - 1, x as i32 + 1) {
                        let char = field[i as usize][j as usize];
                        found_symbol |= !char.is_digit(10) && char != '.'
                    }
                }
            } else {
                if found_symbol {
                    sum += number;
                }
                number = -1;
                found_symbol = false;
            }
        }
        if found_symbol {
            sum += number;
        }
    }

    println!("{}", sum);
}
