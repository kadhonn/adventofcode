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

    for mapping in mapping_list.iter_mut() {
        mapping.sort_by_key(|m| m.0);
    }

    let mut min = i64::MAX;
    for seed_i in 0..(seeds.len() / 2) {
        let start = seeds[seed_i * 2];
        let end = start + seeds[seed_i * 2 + 1];
        min = i64::min(min, find_min_for_range(&mapping_list, 0, start, end));
    }

    println!("{}", min);
}

fn find_min_for_range(mapping_list: &Vec<Vec<(i64, i64, i64)>>, mapping_i: usize, start: i64, end: i64) -> i64 {
    if mapping_i == mapping_list.len() {
        return start;
    }
    let mut min = i64::MAX;
    let mut current_start = start;
    let mapping = &mapping_list[mapping_i];
    for mapping_entry in mapping {
        if mapping_entry.0 > current_start {
            let block_end = i64::min(mapping_entry.0, end);
            min = i64::min(min, find_min_for_range(&mapping_list, mapping_i + 1, current_start, block_end));
            current_start = block_end;
            if current_start == end {
                break;
            }
        } else {
            if current_start < mapping_entry.1 {
                let block_end = i64::min(mapping_entry.1, end);
                min = i64::min(min, find_min_for_range(&mapping_list, mapping_i + 1, current_start - mapping_entry.0 + mapping_entry.2, block_end - mapping_entry.0 + mapping_entry.2));
                current_start = block_end;
                if current_start == end {
                    break;
                }
            }
        }
    }
    if current_start != end {
        min = i64::min(min, find_min_for_range(&mapping_list, mapping_i + 1, current_start, end));
    }
    return min;
}
