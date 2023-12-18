use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;


const NORTH: (i32, i32) = (-1, 0);
const SOUTH: (i32, i32) = (1, 0);
const EAST: (i32, i32) = (0, 1);
const WEST: (i32, i32) = (0, -1);

pub fn day18(str: &str) {
    let mut map = HashSet::new();
    let mut current_field = (0, 0);
    map.insert(current_field);
    for line in str.lines() {
        let split: Vec<&str> = line.split(" ").collect();
        let dir_command = split[0];
        let steps = split[1].parse::<i32>().unwrap();
        let dir = match dir_command {
            "R" => EAST,
            "L" => WEST,
            "U" => NORTH,
            "D" => SOUTH,
            _ => panic!("uh oh")
        };
        for _ in 0..steps * 2 {
            current_field = (current_field.0 + dir.0, current_field.1 + dir.1);
            map.insert(current_field);
        }
    }

    let min_y = map.iter().map(|field| (*field).0).min().unwrap();
    let max_y = map.iter().map(|field| (*field).0).max().unwrap();
    let min_x = map.iter().map(|field| (*field).1).min().unwrap();
    let max_x = map.iter().map(|field| (*field).1).max().unwrap();

    for y in min_y..=max_y {
        for x in min_x..=max_x {
            if map.contains(&(y, x)) {
                print!("#");
            } else {
                print!(".");
            }
        }
        println!()
    }

    let mut internal = HashSet::new();
    for i in 0..200 {
        let maybe = try_find_internal(&map, (i, i), min_y, max_y, min_x, max_x);
        if maybe.is_some() {
            internal = maybe.unwrap();
            if !internal.is_empty() {
                break;
            }
        }
    }

    if internal.is_empty() {
        panic!("fuuu");
    }

    let filtered_map = map.iter().filter(|field| (**field).0 % 2 == 0 && (**field).1 % 2 == 0);
    let filtered_ínternal = internal.iter().filter(|field| (**field).0 % 2 == 0 && (**field).1 % 2 == 0);
    println!("{}", filtered_map.count() + filtered_ínternal.count());
}

fn try_find_internal(map: &HashSet<(i32, i32)>, start: (i32, i32), min_y: i32, max_y: i32, min_x: i32, max_x: i32) -> Option<HashSet<(i32, i32)>> {
    if map.contains(&start) {
        return None;
    }

    let mut inside = HashSet::new();
    let mut to_check = LinkedList::new();
    to_check.push_back(start);

    while !to_check.is_empty() {
        let (y, x) = to_check.pop_front().unwrap();
        if y < min_y || y > max_y || x < min_x || x > max_x {
            return None;
        }
        if inside.contains(&(y, x)) || map.contains(&(y, x)) {
            continue;
        }
        inside.insert((y, x));
        to_check.push_back((y + NORTH.0, x + NORTH.1));
        to_check.push_back((y + SOUTH.0, x + SOUTH.1));
        to_check.push_back((y + WEST.0, x + WEST.1));
        to_check.push_back((y + EAST.0, x + EAST.1));
    }

    return Some(inside);
}