use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::i64::MAX;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use regex::Regex;

pub fn day15(str: &str) {
    let mut boxes = vec![];
    for _ in 0..256 {
        boxes.push(IndexMap::new())
    }

    for string in str.split(",") {
        let add = string.contains('=');
        let split: Vec<&str> = string.split(|c| c == '=' || c == '-').collect();
        let label = split[0];
        let hash = hash(label);
        if add {
            let strength: i32 = split[1].parse().unwrap();
            boxes[hash as usize].insert(label, strength);
        } else {
            boxes[hash as usize].shift_remove(label);
        }
        // println!("{string}");
        // print_and_calc(&mut boxes);
        // println!();
        // println!();
    }

    print_and_calc(&mut boxes);
}

fn print_and_calc(boxes: &mut Vec<IndexMap<&str, i32>>) {
    let mut sum = 0;
    for j in 0..boxes.len() {
        let b = &boxes[j];
        let mut i = 1;
        for (label, strength) in b {
            let power = (j + 1) as i32 * i * strength;
            println!("{}: {} * {} * {} = {}", label, j + 1, i, strength, power);
            sum += power;
            i += 1;
        }
    }

    println!("{sum}");
}

fn hash(string: &str) -> i32 {
    let mut hash = 0;
    for char in string.chars() {
        hash += char as i32;
        hash *= 17;
        hash = hash % 256;
    }
    hash
}