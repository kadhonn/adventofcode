use std::cmp::{max, min};
use std::collections::HashMap;
use std::ops::Shl;

pub fn day4_1(str: &str) {
    let mut sum = 0;

    for line in str.lines() {
        let split = line.split(": ").collect::<Vec<&str>>();
        let group_split = split[1].split("|").collect::<Vec<&str>>();
        let winning_numbers = group_split[0].trim().split(" ").filter(|str| str != &"").map(|number| number.trim().parse::<i32>().unwrap()).collect::<Vec<i32>>();
        let my_numbers = group_split[1].trim().split(" ").filter(|str| str != &"").map(|number| number.trim().parse::<i32>().unwrap()).collect::<Vec<i32>>();

        let mut points = 0;
        for number in my_numbers {
            if winning_numbers.contains(&number) {
                if points == 0 {
                    points = 1;
                } else {
                    points = points.shl(1)
                }
            }
        }

        sum += points;
    }

    println!("{}", sum);
}
