use std::cmp::{max, min};
use std::collections::HashMap;

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

pub fn day3_2(str: &str) {
    let mut map: HashMap<(i32, i32), Vec<i32>> = HashMap::new();
    let field = str.lines().map(|line| line.chars().collect::<Vec<char>>()).collect::<Vec<Vec<char>>>();

    for y in 0..field.len() {
        let mut number = -1;
        let mut coords_of_symbol = vec![];
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
                        if char == '*' {
                            let number = (i, j);
                            if !coords_of_symbol.contains(&number) {
                                coords_of_symbol.push(number);
                            }
                        }
                    }
                }
            } else {
                for coord in &coords_of_symbol {
                    if map.contains_key(&coord) {
                        map.get_mut(&(coord.clone())).unwrap().push(number);
                    } else {
                        let list = vec![number];
                        map.insert(coord.clone(), list);
                    }
                }
                number = -1;
                coords_of_symbol = vec![];
            }
        }
        for coord in &coords_of_symbol {
            if map.contains_key(&coord) {
                map.get_mut(&(coord.clone())).unwrap().push(number);
            } else {
                let list = vec![number];
                map.insert(coord.clone(), list);
            }
        }
    }

    println!("{:?}", &map);
    let mut sum = 0;
    for entry in map {
        if entry.1.len() == 2 {
            sum += entry.1[0] * entry.1[1]
        }
    }
    println!("{}", sum);
}
