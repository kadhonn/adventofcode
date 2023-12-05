use std::cmp::{max, min};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::Shl;

pub fn day5_1(str: &str) {
    let mut mapping_list: Vec<Vec<(i64, i64, i64)>> = vec![];
    let mut seeds: Vec<i64> = vec![];

    for line in str.lines() {
        if seeds.is_empty() {
            for number in (&line[7..line.len()]).split(" ") {
                seeds.push(number.trim().parse::<i64>().unwrap());
            }
            continue;
        }
        if line.is_empty() {
            mapping_list.push(vec![]);
            continue;
        }
        if !line.chars().collect::<Vec<char>>()[0].is_digit(10) {
            continue;
        }
        let numbers = line.split(" ").map(|number| number.trim().parse::<i64>().unwrap()).collect::<Vec<i64>>();
        mapping_list.last_mut().unwrap().push((numbers[1], numbers[1] + numbers[2], numbers[0]));
    }

    let mut min = i64::MAX;
    for seed in seeds {
        let mut current = seed;
        for mapping in &mapping_list {
            'inner: for mapping_entry in mapping {
                if mapping_entry.0 <= current && mapping_entry.1 >= current {
                    current = current - mapping_entry.0 + mapping_entry.2;
                    break 'inner;
                }
            }
        }
        min = i64::min(min, current);
    }

    println!("{}", min);
}

pub fn day5_2(str: &str) {
    let mut mapping_list: Vec<Vec<(i64, i64, i64, i64)>> = vec![];
    let mut seeds: Vec<i64> = vec![];

    for line in str.lines() {
        if seeds.is_empty() {
            for number in (&line[7..line.len()]).split(" ") {
                seeds.push(number.trim().parse::<i64>().unwrap());
            }
            continue;
        }
        if line.is_empty() {
            mapping_list.push(vec![]);
            continue;
        }
        if !line.chars().collect::<Vec<char>>()[0].is_digit(10) {
            continue;
        }
        let numbers = line.split(" ").map(|number| number.trim().parse::<i64>().unwrap()).collect::<Vec<i64>>();
        mapping_list.last_mut().unwrap().push((numbers[1], numbers[1] + numbers[2], numbers[0], numbers[0] + numbers[2]));
    }

    for i in 0..i64::MAX {
        let mut current = i;
        for mapping in mapping_list.iter().rev() {
            'inner: for mapping_entry in mapping {
                if mapping_entry.2 <= current && current <= mapping_entry.3 {
                    current = current - mapping_entry.2 + mapping_entry.0;
                    break 'inner;
                }
            }
        }

        for seed_i in 0..(seeds.len() / 2) {
            let start = seeds[seed_i * 2];
            let range = seeds[seed_i * 2 + 1];
            if start <= current && current < start + range {
                println!("{i}");
                return;
            }
        }
    }
}
