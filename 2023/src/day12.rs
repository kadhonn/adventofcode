use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day12(str: &str) {
    let mut sum = 0;

    for line in str.lines() {
        let split = line.split(" ").collect::<Vec<&str>>();
        let chars = split[0].chars().collect::<Vec<char>>();
        let damaged_groups = split[1].split(",").map(|n| n.parse::<i32>().unwrap()).collect::<Vec<i32>>();

        let mut valid_count = 0;
        let mut stack = LinkedList::new();

        println!();
        println!();
        println!("{}", line);
        recursive_build(&chars, &damaged_groups, &mut stack, &mut valid_count);

        println!("{valid_count}");
        sum += valid_count;
    }

    println!("{sum}");
}

fn recursive_build(chars: &Vec<char>, damaged_groups: &Vec<i32>, stack: &mut LinkedList<char>, valid_count: &mut i32) {
    if !is_still_valid(chars, damaged_groups, stack) {
        return;
    }
    if chars.len() == stack.len() {
        *valid_count += 1;
        println!("{}", stack.iter().collect::<String>());
        return;
    }

    if chars[stack.len()] == '?' {
        stack.push_back('#');
        recursive_build(chars, damaged_groups, stack, valid_count);
        stack.pop_back();
        stack.push_back('.');
        recursive_build(chars, damaged_groups, stack, valid_count);
        stack.pop_back();
    } else {
        stack.push_back(chars[stack.len()]);
        recursive_build(chars, damaged_groups, stack, valid_count);
        stack.pop_back();
    }
}

fn is_still_valid(chars: &Vec<char>, damaged_groups: &Vec<i32>, stack: &LinkedList<char>) -> bool {
    let solution = stack.iter().collect::<Vec<&char>>();
    let mut group_i = 0;
    let mut current_group_size = 0;
    let mut in_group = false;

    for i in 0..solution.len() {
        let char = solution[i];
        if !in_group && *char == '.' {
            continue;
        } else if !in_group && *char == '#' {
            if group_i == damaged_groups.len() {
                return false;
            }
            current_group_size = 1;
            in_group = true;
        } else if in_group && *char == '#' {
            current_group_size += 1;
        } else if in_group && *char == '.' {
            if current_group_size != damaged_groups[group_i] {
                return false;
            }
            group_i += 1;
            in_group = false;
        } else {
            panic!("unknown constellation");
        }
    }

    return if solution.len() == chars.len() {
        (!in_group && group_i == damaged_groups.len()) ||
            (in_group && group_i == damaged_groups.len() - 1 && current_group_size == damaged_groups[group_i])
    } else {
        true
    };
}
