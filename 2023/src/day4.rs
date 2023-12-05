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

pub fn day4_2(str: &str) {
    let mut sum = 0;
    let mut copies = vec![1];

    for line in str.lines() {
        let split = line.split(": ").collect::<Vec<&str>>();
        let group_split = split[1].split("|").collect::<Vec<&str>>();
        let winning_numbers = group_split[0].trim().split(" ").filter(|str| str != &"").map(|number| number.trim().parse::<i32>().unwrap()).collect::<Vec<i32>>();
        let my_numbers = group_split[1].trim().split(" ").filter(|str| str != &"").map(|number| number.trim().parse::<i32>().unwrap()).collect::<Vec<i32>>();

        let mut matching_numbers = 0;
        for number in my_numbers {
            if winning_numbers.contains(&number) {
                matching_numbers += 1;
            }
        }

        let copy_count = if copies.is_empty() { 1 } else { copies.remove(0) };

        sum += copy_count;
        for i in 0..matching_numbers {
            if i < copies.len() {
                copies[i] = copies[i] + copy_count
            } else {
                copies.push(copy_count + 1);
            }
        }
    }

    println!("{}", sum);
}
