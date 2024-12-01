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

pub fn day23(str: &str) {
    let map: Vec<Vec<char>> = str.lines().map(|line| line.chars().collect()).collect();
    let mut start = (0, 0);
    let mut end = (0, 0);
    for x in 0..map[0].len() {
        if map[0][x] == '.' {
            start = (0, x as i32);
        }
        if map[map.len() - 1][x] == '.' {
            end = ((map.len() - 1) as i32, x as i32);
        }
    }

    println!("{:?}", start);
}
