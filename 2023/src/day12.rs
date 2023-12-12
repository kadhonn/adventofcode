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
        let orig_chars = split[0].chars().collect::<Vec<char>>();
        let orig_damaged_groups = split[1].split(",").map(|n| n.parse::<i32>().unwrap()).collect::<Vec<i32>>();

        let mut chars = vec![];
        let mut damaged_groups = vec![];
        for _ in 0..5 {
            for char in &orig_chars {
                chars.push(char.clone());
            }
            chars.push('?');
            for damaged_group in &orig_damaged_groups {
                damaged_groups.push(damaged_group.clone());
            }
        }
        chars.remove(chars.len() - 1);

        let mut stack = LinkedList::new();
        let mut cache = HashMap::new();

        // println!();
        // println!();
        // println!("{}", line);

        let mut valid_count = recursive_build(&chars, &damaged_groups, &mut stack, &mut cache);

        println!("{valid_count}");
        sum += valid_count;
    }

    println!("{sum}");
}

fn recursive_build(chars: &Vec<char>, damaged_groups: &Vec<i32>, stack: &mut LinkedList<char>, cache: &mut HashMap<(usize, i32), i64>) -> i64 {
    let (valid, group_i) = is_still_valid(chars, damaged_groups, stack);
    if !valid {
        return 0;
    }

    if chars.len() == stack.len() {
        // println!("{}", stack.iter().collect::<String>());
        return 1;
    }

    if let Some(entry) = cache.get(&(stack.len(), group_i)) {
        return *entry;
    }

    let mut valid_count = 0;
    if chars[stack.len()] == '?' {
        stack.push_back('#');
        valid_count += recursive_build(chars, damaged_groups, stack, cache);
        stack.pop_back();
        stack.push_back('.');
        valid_count += recursive_build(chars, damaged_groups, stack, cache);
        stack.pop_back();
    } else {
        stack.push_back(chars[stack.len()]);
        valid_count += recursive_build(chars, damaged_groups, stack, cache);
        stack.pop_back();
    }
    if group_i != -1 {
        cache.insert((stack.len(), group_i), valid_count);
    }
    return valid_count;
}

fn is_still_valid(chars: &Vec<char>, damaged_groups: &Vec<i32>, stack: &LinkedList<char>) -> (bool, i32) {
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
                return (false, -1);
            }
            current_group_size = 1;
            in_group = true;
        } else if in_group && *char == '#' {
            current_group_size += 1;
        } else if in_group && *char == '.' {
            if current_group_size != damaged_groups[group_i] {
                return (false, -1);
            }
            group_i += 1;
            in_group = false;
        } else {
            panic!("unknown constellation");
        }
    }

    return if solution.len() == chars.len() {
        ((!in_group && group_i == damaged_groups.len()) ||
             (in_group && group_i == damaged_groups.len() - 1 && current_group_size == damaged_groups[group_i]), group_i as i32)
    } else {
        if in_group {
            (true, -1)
        } else {
            (true, group_i as i32)
        }
    };
}
