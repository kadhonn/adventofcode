#![allow(dead_code)]

use std::fs;

mod day9;

fn main() {
    // let str = fs::read_to_string("in/in9_ex.txt").unwrap();
    let str = fs::read_to_string("in/in9.txt").unwrap();
    day9::day9_2(&str.trim());
}