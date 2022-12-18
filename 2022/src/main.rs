#![allow(dead_code)]

use std::fs;

mod day12;

fn main() {
    // let str = fs::read_to_string("in/in12_ex.txt").unwrap();
    let str = fs::read_to_string("in/in12.txt").unwrap();
    day12::day12_2(&str.trim());
}