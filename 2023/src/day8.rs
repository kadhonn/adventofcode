use std::cmp::{max, min, Ordering};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::{Index, Shl};
use regex::Regex;

pub fn day8(str: &str) {
    let mut map = HashMap::new();
    let regex = Regex::new(r"(\w{3}) = \((\w{3}), (\w{3})\)").unwrap();

    let instructions = str.lines().next().unwrap().chars().collect::<Vec<char>>();

    for line in str.lines().skip(2) {
        for group in regex.captures_iter(line) {
            let begin = group.get(1).unwrap().as_str();
            let left = group.get(2).unwrap().as_str();
            let right = group.get(3).unwrap().as_str();
            map.insert(begin, (left, right));
        }
    }

    let mut current = "AAA";
    for i in 0..usize::MAX {
        if current == "ZZZ" {
            println!("{}", i);
            break;
        }
        let current_instruction = instructions[i % instructions.len()];
        let next = if current_instruction == 'L' {
            map[current].0
        } else {
            map[current].1
        };
        current = next;
    }
}
