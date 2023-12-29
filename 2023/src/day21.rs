use std::cell::RefCell;
use std::cmp::{max, min, Ordering};
use std::collections::{HashMap, HashSet, LinkedList};
use std::f32::consts::E;
use std::i64::MAX;
use std::io::BufRead;
use std::ops::{Index, Shl};
use indexmap::IndexMap;
use num_bigint::{BigInt, Sign};
use priority_queue::PriorityQueue;
use regex::Regex;


const NORTH: (i32, i32) = (-1, 0);
const SOUTH: (i32, i32) = (1, 0);
const EAST: (i32, i32) = (0, 1);
const WEST: (i32, i32) = (0, -1);

pub fn day21(str: &str) {
    let mut map: Vec<Vec<char>> = str.lines().map(|line| line.chars().collect()).collect();
    let mut start = (0, 0);
    'outer: for y in 0..map.len() {
        for x in 0..map[y].len() {
            if map[y][x] == 'S' {
                map[y][x] = '.';
                start = (y as i32, x as i32);
                break 'outer;
            }
        }
    }

    let mut visited = HashSet::new();
    let mut count = 0i64;
    let mut current = HashSet::new();
    current.insert(start);
    let goal = 131*2 *4;
    // let goal = 500;
    for i in 0..goal {
        let mut new = HashSet::new();
        for pos in current {
            let dirs = vec![NORTH, SOUTH, WEST, EAST];
            for dir in dirs {
                let new_pos = (pos.0 + dir.0, pos.1 + dir.1);
                let mut normalized_y = new_pos.0 % map.len() as i32;
                if normalized_y < 0 {
                    normalized_y += map.len() as i32;
                }
                let mut normalized_x = new_pos.1 % map[normalized_y as usize].len() as i32;
                if normalized_x < 0 {
                    normalized_x += map[normalized_y as usize].len() as i32;
                }
                if map[normalized_y as usize][normalized_x as usize] == '.' {
                    if !visited.contains(&new_pos) {
                        visited.insert(new_pos);
                        new.insert(new_pos);
                        if (goal + 0 - i) % 2 == 0 {
                            count += 1;
                        }
                    }
                }
            }
        }
        current = new;
        if (i + 1) % (131*2) == 0 {
            println!("{count}");
        }
    }
    println!("{count}");
}